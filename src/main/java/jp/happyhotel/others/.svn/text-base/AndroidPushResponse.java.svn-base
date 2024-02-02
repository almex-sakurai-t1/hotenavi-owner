package jp.happyhotel.others;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AndroidPushResponse implements Serializable
{
    /**
     *
     */
    private static final long     serialVersionUID = -4992208779604047043L;
    public long                     multicast_id;
    public int                      success;
    public int                      failure;
    public int                      canonical_ids;
    public List<AndroidPushResponseResults> results;
}
