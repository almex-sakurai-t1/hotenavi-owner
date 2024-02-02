package jp.happyhotel.mailsearch;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Properties;

import jp.happyhotel.search.SearchHotelCommon;
import jp.happyhotel.search.SearchHotelFreeWord;
import jp.happyhotel.search.SearchHotelService;
import jp.happyhotel.sponsor.SponsorData;
import jp.happyhotel.user.UserBasicInfo;
import jp.happyhotel.common.Logging;
import jp.happyhotel.data.DataHotelBasic;

public class MailHandler {

	/**
	 * @
	 * @author kapil agarwal
	 */

	private EmailMessage emailMessage;

	private String mailAddress;

	private String subject;

	private int[] fixedWordIdFoundInKeyword;

	private String freeWordToSearch;

	private int fixedWordCount = 0;

	private UserBasicInfo userBasicInfo;

	private SponsorData sponsorData;

	private SearchHotelFreeWord searchHotelFreeWord;

	private SearchHotelService searchHotelService;

	private SearchHotelCommon searchHotelCommon;

	private static Properties props;

	private String moreLink = null;

    // these messages should be read from the configuartion file

	private static String SEARCH_RESULT_HEADING;

	private static String SPONSOR_RESULT_HEADING;

	private static String RESULTS_MORE_THAN_200;

	private static String NO_RESULT_FOUND;

	private static String AUTH_FAILURE;

	private static String NO_KEYWORD_PRESENT;

	private static String MOBILE_USER_DOMAIN;

	private static String SPONSORED_LINK_MOBILE;

	private static String HOTEL_DETAIL_LINK_MOBILE;

	private static String MORE_LINK_MOBILE;

	private static String SPONSORED_LINK_PC;

	private static String HOTEL_DETAIL_LINK_PC;

	private static String MORE_LINK_PC;

	private static String listOfFixedWords;

	private static String arrFixedWords[];

	private static int arrFixedWordId[];

	private static String arrFixedWordURLParameter[];

	private String errorMessage;

	private int totalNumberOfRecords = 0;

	private boolean resultsFromFixedWordAndFreeWord;

	private boolean resultsFromFixedWord;

	private boolean resultsFromFreeWord;

	static {
		// read error messages from the configuration file
		props = MailSearch.getProperties();
		RESULTS_MORE_THAN_200 = toCharSet(props
				.getProperty("error.results_more_than_200"));
		NO_RESULT_FOUND = toCharSet(props
				.getProperty("error.no_result_found"));
		AUTH_FAILURE = toCharSet(props
				.getProperty("error.auth_failure"));
		NO_KEYWORD_PRESENT = toCharSet(props
				.getProperty("error.no_keyword_entered_by_the_user"));
		SPONSOR_RESULT_HEADING = toCharSet(props
				.getProperty("heading.sponsor_link"));
		SEARCH_RESULT_HEADING = toCharSet(props
				.getProperty("heading.search_result"));
		listOfFixedWords = toCharSet(props.getProperty("search.fixedwords"));
		parseFixedWordsList();


		MOBILE_USER_DOMAIN=props.getProperty("MOBILE_USER_DOMAIN");
		SPONSORED_LINK_MOBILE=props.getProperty("SPONSORED_LINK_MOBILE");
		SPONSORED_LINK_PC=props.getProperty("SPONSORED_LINK_PC");
		HOTEL_DETAIL_LINK_MOBILE=props.getProperty("HOTEL_DETAIL_LINK_MOBILE");
		MORE_LINK_MOBILE=props.getProperty("MORE_LINK_MOBILE");
		HOTEL_DETAIL_LINK_PC=props.getProperty("HOTEL_DETAIL_LINK_PC");
		MORE_LINK_PC=props.getProperty("MORE_LINK_PC");


		Logging.info("RESULTS_MORE_THAN_200 -> " + RESULTS_MORE_THAN_200);
		Logging.info("NO_RESULT_FOUND -> " + NO_RESULT_FOUND);
		Logging.info("AUTH_FAILURE -> " + AUTH_FAILURE);
		Logging.info("Fixed Words -> " + listOfFixedWords);
		Logging.info("SEARCH_RESULT_HEADING -> " + SEARCH_RESULT_HEADING);
		Logging.info("SPONSOR_RESULT_HEADING -> " + SPONSOR_RESULT_HEADING);
		Logging.info("NO_KEYWORD_PRESENT -> " + NO_KEYWORD_PRESENT);

	}

	// Initialize
	public MailHandler(EmailMessage emailMessage) {

		this.emailMessage = emailMessage;
		this.mailAddress = emailMessage.getFromMailAddr();
		this.subject = emailMessage.getSubject();

		userBasicInfo = new UserBasicInfo();
		sponsorData = new SponsorData();
		searchHotelFreeWord = new SearchHotelFreeWord();

		resultsFromFixedWordAndFreeWord = false;
		resultsFromFixedWord = false;
		resultsFromFreeWord = false;

		errorMessage = null;

	}
	/**
	 * for reading japanese characters.
	 * @param str
	 * @return
	 */
	private static String toCharSet(String str) {
		String result = null;
		if (str != null) {
			try {
				 result = new String(str.getBytes("ISO-8859-1"), "UTF-8");
			} catch (Exception ex) {
				// do nothing
			}
		}

		return result;
	}

	/**
	 * check whether the user is registered or not on the basis of email address
	 *
	 * @param mailaddr
	 *            of the user
	 * @return true - if the user is registered false - if the user is not
	 *         registered
	 */
	private boolean checkUser(String mailAddress) {
		boolean isValidUser;
		isValidUser = userBasicInfo.getUserBasicByMailaddr(mailAddress);

		if (isValidUser == false) {
			errorMessage = AUTH_FAILURE;
		}

		return isValidUser;

	}

	/**
	 * parses the list of fixed words read from configuration file separated by
	 * semi-colon (;) and puts the fixed word and their corresponding Id into
	 * separate arrays
	 * （[0]:クレジット,[1]:駐車場,[2]:クーポン,[3]:外出,[4]:予約,[5]:１人,[6]:３人,[7]:ルームサービス,[8]:精算機,[9]:空室）
	 */
	private static void parseFixedWordsList() {
		String fixedWordListTokens[];
		String fixedWordTokens[];
		String fixedWordAndId;

		if (listOfFixedWords != null) {
			fixedWordListTokens = listOfFixedWords.split(";");

			arrFixedWords = new String[fixedWordListTokens.length];
			arrFixedWordId = new int[fixedWordListTokens.length];
			arrFixedWordURLParameter = new String[10];

			for (int index = 0; index < fixedWordListTokens.length; index++) {
				fixedWordAndId = fixedWordListTokens[index];
				fixedWordTokens = fixedWordAndId.split("-");
				arrFixedWords[index] = fixedWordTokens[0];
				arrFixedWordId[index] = Integer.parseInt(fixedWordTokens[1]);

				if (arrFixedWordId[index] < 10) {
					arrFixedWordURLParameter[Integer
							.parseInt(fixedWordTokens[1])] = fixedWordTokens[2];
				} else {
					Logging.warn(">>>>>>> Id for fixed word greater than 9 ->["
							+ fixedWordAndId + "]");
				}

			}
		}

	}

	/**
	 * check whether the specified word is a fixed word or not
	 *
	 * @param keyword
	 * @return Id of the fixed word else returns -1 if the specified keyword is
	 *         not a fixed word
	 */
	private int isFixedWord(String keyword) {
		int Id = -1;

		if (arrFixedWords != null) {
			for (int index = 0; index < arrFixedWords.length; index++) {
				if (keyword.equalsIgnoreCase(arrFixedWords[index])) {
					Id = arrFixedWordId[index];
					break;
				}
			}
		}

		return Id;
	}

	/**
	 * search hotels which provide the services requested by the user
	 * （[0]:クレジット,[1]:駐車場,[2]:クーポン,[3]:外出,[4]:予約,[5]:１人,[6]:３人,[7]:ルームサービス,[8]:精算機,[9]:空室）
	 *
	 * @param service
	 *            array specifying the equipment list
	 * @return array of hotel id
	 */
	private int[] searchHotelsWithFixedWords(int service[]) {
		int[] hotelIdList = null;
		boolean resultFound;

		if (service != null) {
			searchHotelService = new SearchHotelService();
			resultFound = searchHotelService.getHotelList(service, 10, 0);
			if (resultFound == true) {

				Logging.debug("Number of records found (FixedWordSearch) :"
						+ searchHotelService.getCount());
				Logging
						.debug("Total number of records found (FixedWordSearch) :"
								+ searchHotelService.getAllCount());
				hotelIdList = searchHotelService.getHotelIdList();
			}

		}

		return hotelIdList;
	}

	/**
	 * search hotels which matches the input string (free word search)
	 *
	 * @param freeWordToSearch
	 *            words to be matched
	 * @return id of the hotels which matches the input string
	 */
	private int[] searchHotelsWithFreeWords(String freeWordToSearch) {
		int[] hotelIdList = null;
		boolean resultFound;

		if (freeWordToSearch != null) {
			freeWordToSearch = freeWordToSearch.trim();
			Logging.debug("Free Word Search string ->" + freeWordToSearch);
			resultFound = searchHotelFreeWord.getHotelList(freeWordToSearch,
					11, 0);

			if (resultFound == true) {
				Logging.debug("Number of records found (FreeWordSearch) :"
						+ searchHotelFreeWord.getCount());
				Logging
						.debug("Total number of records found (FreeWordSearch) :"
								+ searchHotelFreeWord.getAllCount());
				hotelIdList = searchHotelFreeWord.getHotelIdList();
			}

		}

		return hotelIdList;
	}

	/**
	 * this function parses the keyword string in the subject line of the e-mail
	 * received from the user separates the fixed words and concatenates the non
	 * fixed words separated by a space into a single string
	 *
	 * @param keyword
	 *            string extracted from the subject line of the e-mail received
	 *            from the user
	 */
	private void parseKeyword(String keyword) {
		String tokens[];
		int Id;

		tokens = keyword.split(" ");

		for (int index = 0; index < tokens.length; index++) {
			Id = isFixedWord(tokens[index]);

			// is a fixed word
			if (Id != -1) {
				if (fixedWordIdFoundInKeyword == null) {
					// size fixed because of check in SearchHotelService
					fixedWordIdFoundInKeyword = new int[10];
				}

				if (fixedWordCount <= 9) {
					fixedWordIdFoundInKeyword[Id] = 1; // set the status of the
														// service
				}
			}
			// is a free word
			else {
				if (freeWordToSearch == null) {
					freeWordToSearch = "";
				}
				freeWordToSearch = freeWordToSearch + " " + tokens[index];

			}
		} // End of for loop

		if (freeWordToSearch != null) {
			freeWordToSearch = freeWordToSearch.trim();
		}
	}

	/**
	 * find hotels for the given word
	 *
	 * @param freeWord
	 *            words entered by the user in the subject line
	 * @return
	 */

	private ArrayList<String> searchHotels() throws UnsupportedEncodingException {
		int[] fixedWordHotelIdList;
		int[] freeWordHotelIdList;

		String hotelName;
		String hotelDetails;
		int hotelId;

		ArrayList<String> searchResults = null;

		DataHotelBasic dhb[];

		int numOfRecords;

		parseKeyword(subject);

		fixedWordHotelIdList = searchHotelsWithFixedWords(fixedWordIdFoundInKeyword);
		freeWordHotelIdList = searchHotelsWithFreeWords(freeWordToSearch);

		// merge the two hotel lists
		if ((fixedWordHotelIdList != null) && (freeWordHotelIdList != null)) {
			searchHotelCommon = new SearchHotelCommon();
			searchHotelCommon.setEquipHotelList(fixedWordHotelIdList);
			searchHotelCommon.setResultHotelList(freeWordHotelIdList);

			searchHotelCommon.getMargeHotel(10, 0, true);
			totalNumberOfRecords = searchHotelCommon.getAllCount();
			numOfRecords = searchHotelCommon.getCount();

			dhb = searchHotelCommon.getHotelInfo();

			resultsFromFixedWordAndFreeWord = true;

		} else if (fixedWordHotelIdList != null) {
			numOfRecords = searchHotelService.getCount();
			totalNumberOfRecords = searchHotelService.getAllCount();
			dhb = searchHotelService.getHotelInfo();

			resultsFromFixedWord = true;
		} else {
			numOfRecords = searchHotelFreeWord.getCount();
			totalNumberOfRecords = searchHotelFreeWord.getAllCount();
			dhb = searchHotelFreeWord.getHotelInfo();

			resultsFromFreeWord = true;
		}

		if (dhb != null && dhb.length > 0) {
			searchResults = new ArrayList<String>();

			for (int count = 0; count < numOfRecords; count++) {
				hotelName = dhb[count].getName();
				hotelId = dhb[count].getId();
				hotelDetails = hotelName + ";" + hotelId;

				searchResults.add(hotelDetails);

			}
		}

		return searchResults;

	}

	/**
	 * returns the list of sponsors present in a pref
	 *
	 * @param prefId
	 *            prefId to which user belongs
	 * @return Array list of string containing the details of the sponsors
	 *         separated by semicolon (;)
	 */
	private ArrayList<String> searchSponsor(int prefId)
			throws UnsupportedEncodingException {
		ArrayList<String> sponsorSearchResult = null;

		String sponsorDetails = null;
		String title = null;
		int hotelId;

		boolean isSponsorExist;

		isSponsorExist = sponsorData.getSponsorByPref(prefId);

		if (isSponsorExist != false && sponsorData.getSponsorCount() > 0) {
			sponsorSearchResult = new ArrayList<String>();

			Logging.debug("Number of Sponsors found :"
					+ sponsorData.getSponsorCount());
			for (int j = 0; j < sponsorData.getSponsorCount(); j++) {
				hotelId = sponsorData.getSponsor()[j].getHotelId();
				title = sponsorData.getSponsor()[j].getTitle();
				sponsorDetails = hotelId + ";" + title;
				sponsorSearchResult.add(sponsorDetails);
			}

		}

		return sponsorSearchResult;
	}

	/**
	 * search for sponsors and format the result in html
	 *
	 * @return String results of sponsor search
	 */
	private String createSponsorMessage(String sponsorLink) {

		String tokens[] = null;

		String hotelId;
		String title;
        String sponsorURL=sponsorLink;
		String sponsorResult = null;
		StringBuffer buffer;

		try {
			int prefId = userBasicInfo.getUserInfo().getPrefCode();

			ArrayList<String> sponsorSearchResults = searchSponsor(prefId);

			if (sponsorSearchResults != null) {

				buffer = new StringBuffer(SPONSOR_RESULT_HEADING)
						.append("<br>");

				for (int j = 0; j < sponsorSearchResults.size(); j++)

				{
					tokens = sponsorSearchResults.get(j).split(";");
					hotelId = tokens[0];
					title = tokens[1];

					buffer.append("<LI><a href=\"").append(sponsorURL).append(
							hotelId).append("\">").append(title).append(
							"</a><br>");

				}// End of for loop

				sponsorResult = buffer.toString();
			}
		} catch (Exception ex) {
			Logging.error(ex.getMessage());
		}

		return sponsorResult;
	}

	/**
	 * search for hotels and format the result in html
	 *
	 * @return string combined results of free word + fixed word search
	 */
	private String createFreeWordSearchResultMessage(String hotelDetailPageLink) {

		String tokens[];

		String hotelName = null;
		String hotelId;
		String hotelDetailsURL=hotelDetailPageLink;
		String searchResult = null;
		StringBuffer buffer;

		try {
			ArrayList<String> searchResults = searchHotels();

			if (searchResults != null && searchResults.size() > 0) {
				buffer = new StringBuffer();
				buffer.append("「").append(subject).append("」").append(SEARCH_RESULT_HEADING).append("<br>");

				for (int index = 0; index < searchResults.size(); index++) {

					tokens = searchResults.get(index).split(";");

					hotelName = tokens[0];
					hotelId = tokens[1];

					buffer.append("<LI><a href=\"").append(hotelDetailsURL)
							.append(hotelId).append("\">").append(hotelName)
							.append("</a><br>");

				}

				searchResult = buffer.toString();
			}
		} catch (Exception ex) {
			Logging.error(ex.getMessage());
		}

		return searchResult;

	}

	/**
	 * handles the e-mail message
	 *
	 * @return reply to be sent to the user
	 */

	public String handleMessage() {
		String result = null;
		String freeWordSearchResults = null;
		String sponsorResults = null;

		Logging.info("Going to handle message -> "+emailMessage.getMesgId()+
						"("+mailAddress+","+
						subject+")");
		if (checkUser(mailAddress) != false) {

			if (subject != null && subject.trim().length() > 0) {


				String domain = mailAddress.substring(mailAddress.indexOf("@")+1);
				String moreBaseURL=null;
				String hotelDetailPageLink=null;
				String sponsorLink=null;

				if((MOBILE_USER_DOMAIN).indexOf(domain)>0){
					hotelDetailPageLink=HOTEL_DETAIL_LINK_MOBILE;
					sponsorLink=SPONSORED_LINK_MOBILE;
					moreBaseURL=MORE_LINK_MOBILE;
				}
				else{
					hotelDetailPageLink=HOTEL_DETAIL_LINK_PC;
					sponsorLink=SPONSORED_LINK_PC;
					moreBaseURL=MORE_LINK_PC;
				}

				freeWordSearchResults = createFreeWordSearchResultMessage(hotelDetailPageLink);
				sponsorResults = createSponsorMessage(sponsorLink);

				if ((freeWordSearchResults != null) && (sponsorResults != null)) {
					result = sponsorResults + "<br><br>"
							+ freeWordSearchResults;
				} else if (sponsorResults != null) {
					result = sponsorResults;
				} else if (freeWordSearchResults != null) {
					result = freeWordSearchResults;
				}

				if (totalNumberOfRecords == 0) {
					errorMessage = NO_RESULT_FOUND;
				} else if (totalNumberOfRecords > 200) {
					errorMessage = RESULTS_MORE_THAN_200;
				} else if (totalNumberOfRecords > 10) {
					if (moreLink == null) {
						moreLink = moreBaseURL;
					}

					if ((resultsFromFixedWordAndFreeWord == true)
							|| (resultsFromFixedWord == true)) {
						for (int index = 0; index < arrFixedWordURLParameter.length; index++) {
							if (fixedWordIdFoundInKeyword[index] == 1
									&& arrFixedWordURLParameter[index] != null) {
								moreLink = moreLink
										+ arrFixedWordURLParameter[index] + "&";
							}
						}// End of for loop

					}

					if ((resultsFromFixedWordAndFreeWord == true)
							|| (resultsFromFreeWord == true)) {
						try {

							String encodedString = URLEncoder.encode(
									freeWordToSearch, "Shift-JIS");
							moreLink = moreLink+encodedString;
						} catch (Exception ex) {
							// do nothing
							moreLink = moreLink+freeWordToSearch;
						}
					}

					result = result + "<br><br><a href=\"" + moreLink
							+ "\">もっと結果を見る</a>";

				}

			}// end of if (subject != null && subject.trim().length() > 0)

			else {
				errorMessage = NO_KEYWORD_PRESENT;
			}
		}// end of if(checkUser(mailAddress) !=false)
		else {
			errorMessage = AUTH_FAILURE;

		}

		if (errorMessage != null) {
			result = errorMessage;
			emailMessage.setErrorMessage(errorMessage);
			emailMessage.setStatus(EmailMessage.FAILURE);
		}

		return result;
	}

}
