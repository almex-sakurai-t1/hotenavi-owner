<%@ page contentType="text/html; charset=Windows-31J" %>
<%@ page errorPage="error.jsp" %>

<jsp:useBean id="ownerinfo" scope="session" class="com.hotenavi2.owner.OwnerInfo" />
          <td width="26"><img src="../../common/pc/image/spacer.gif" width="26" height="40"></td>
          <td width="332" align="center" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td>
                  <table width="332" height="33" border="0" cellpadding="0" cellspacing="0">
                    <tr>
                      <td align="left" valign="top"><img src="../../common/pc/image/info.gif" alt="インフォメーション" name="info" width="332" height="33" id="info"></td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr>
                <td valign="top">
                  <table width="332" border="0" cellpadding="2" cellspacing="3" bgcolor="#FFFFFF">

<jsp:include page="information_message.jsp" />

			<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/hp20160106_owner.html','バージョンアップ情報','scrollbars=yes,width=640,height=710')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;バージョンアップ情報（2016/1/6）</strong></a>　<img src="../../common/image/new2.gif" border="0"/></td>
                    </tr>
<!--
					<tr>
                      <td bgcolor="#FF0000" class="size14">
					  <a href="javascript:;" onClick="MM_openBrWindow('/common/pc/mainte20120619_owner.html','回線接続障害のお知らせ','scrollbars=yes,width=640,height=400')" >
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;回線接続障害のお知らせ(2012/6/19 PM15:30～)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#FF0000" class="size14">
					  <a href="javascript:;" onClick="MM_openBrWindow('../../common/pc/nenmatsu2011.html','年末年始休業のお知らせ','scrollbars=yes,width=640,height=500')" >
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;年末年始休業のお知らせ</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#FF0000" class="size14">
					  <a href="javascript:;" onClick="MM_openBrWindow('/common/pc/mainte20120406_owner.html','メンテナンスのお知らせ','scrollbars=yes,width=640,height=400')" >
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;メンテナンスのお知らせ(2012/4/17 AM1:00～)</strong>
					  </a></td>
                    </tr>

					<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/hp20130904_owner.html','日報・月報メール配信サービス遅延障害発生のご報告とお詫び【復旧済】','scrollbars=yes,width=640,height=650')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;日報・月報メール配信サービス遅延障害発生のご報告とお詫び【復旧済】(2013/08/27)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/hp20110726_owner.html','QRCODEを利用した会員情報登録参照機能について','scrollbars=yes,width=640,height=650')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;QRコードを利用した会員情報登録・参照機能(2011/7/26)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/hp20110614_owner.html','スマートフォン向けオーナーサイト','scrollbars=yes,width=640,height=650')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;スマートフォン向けオーナーサイト(2011/6/14)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/hp20110228_owner.html','閲覧時にエラーになる現象','scrollbars=yes,width=640,height=530')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;帳票管理：閲覧時にエラーになる現象について(2011/2/28)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#FF0000" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/fuei20110125_owner.html','ホテル営業形態調査ご協力のご案内','scrollbars=yes,width=640,height=590')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;ホテル営業形態調査　ご協力のご案内(2011/1/25)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/iten20101012_owner.html','移転のご案内','scrollbars=yes,width=640,height=500')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;コンテンツ本部アイメディア課　移転のご案内(2010/10/18)</strong>
					  </a></td>
                    </tr>
					<tr>
                      <td bgcolor="#0066FF" class="size12">
					  <a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/info20100817_owner.html','合併に関するご案内','scrollbars=yes,width=640,height=690')">
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;株式会社アルメックスとの合併に関するご案内</strong>
					  </a></td>
                    </tr>
-->
<!--
					<tr>
                      <td bgcolor="#FF0000" class="size14">
					  <a href="javascript:;" onClick="MM_openBrWindow('/common/pc/mainte20070515_owner.html','NTT回線障害のお知らせ','scrollbars=yes,width=640,height=400')" >
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;NTT回線障害のお知らせ</strong>
					  </a></td>
                    </tr>
-->
<!--
					<tr>
                      <td bgcolor="#0066FF" class="size14">
					  <a href="javascript:;" onClick="MM_openBrWindow('/common/pc/info20081028_owner.html','部屋情報追加機能のお知らせ','scrollbars=yes,width=640,height=510')" >
					  <strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;部屋情報　追加機能のお知らせ</strong>
					  </a></td>
                    </tr>
-->
<!--
                    <tr>
                      <td bgcolor="#0066FF" class="size14"><strong style="color:#FFFFFF; margin:'2' '2' '2' '2'">&nbsp;耳寄りなお知らせ</strong></td>
                    </tr>
                    <tr>
                    		<td bgcolor="#B9D9D9" class="size12" style="margin:'2' '2' '2' '2'">
													<span style="color:#000066 "><strong>ホテナビクーポン掲載募集中!</strong><br>
													詳しくは<a href="http://www.hotenavi.com/hotenavi/abouttokudane.html" target="_blank"><span style="color:#CC0000; font-weight:bold;">→こちら！</span></a>
													</span>
												</td>
                   	</tr>
-->
                    <tr>
                       <td height="4"><img src="../../common/pc/image/spacer.gif" width="100" height="2"></td>
                     </tr>
										<tr>
                      <td class="size14"><strong>パスワードを忘れた場合</strong></td>
                    </tr>
                    <tr>
                      <td class="size12"><p>登録したメンバーユーザー名を記入し下記アドレス宛にメール、またはお電話で「パスワード紛失」の件でお知らせください。<br>
                          メール ： <a href="mailto:imedia-info@hotenavi.com" class="size12"><font color="#FF9900">imedia-info@hotenavi.com</font></a><font color="#FF9900"> </font><br>
                          電　話 ： 03-6820-1480<br>
				          F A X  ： 03-6741-4605<br>
						  </p>      
				      </td>
                    </tr>
<!--
                    <tr>
                      <td height="4"><img src="../../common/pc/image/spacer.gif" width="100" height="2"></td>
                    </tr>
                    <tr>
                      <td class="size14"><strong>更新のお知らせ</strong></td>
                    </tr>
                    <tr>
                      <td class="size12"><a href="javascript:;" onMouseOver="MM_swapImage('yajirushi','','/common/pc/image/yaji_big_o.gif',1)" onMouseOut="MM_swapImgRestore()"><img src="../../common/pc/image/yaji_big.gif" name="yajirushi" width="15" height="13" border="0" align="absmiddle" id="yajirushi" onClick="MM_openBrWindow('/common/pc/update.html','更新のお知らせ','scrollbars=yes,width=640')"></a><img src="../../common/pc/image/spacer.gif" width="6" height="12" align="absmiddle"><a href="javascript:;" class="size12" onClick="MM_openBrWindow('../../common/pc/update.html','更新のお知らせ','scrollbars=yes,resizable=yes,width=640')" onmouseover="MM_swapImage('yajirushi','','/common/pc/image/yaji_big_o.gif',1);" onMouseOut="MM_swapImgRestore()"><font color="#FF9900">新しい更新のお知らせがあります</font></a></td>
                    </tr>
-->
                     <tr>
                       <td class="size12"><img src="../../common/pc/image/spacer.gif" width="100" height="8"></td>
                     </tr>
                  </table>
			    </td>
              </tr>
            </table>
		  </td>
          <td align="left" valign="top">&nbsp;</td>

