package jp.happyhotel.common;

import java.util.HashMap;
import java.util.Map;

public class ConfigClass
{
    Map<String, ActionClass> actionMap = null;

    ConfigClass()
    {
        actionMap = new HashMap<String, ActionClass>();
    }

    public void addAction(String key, ActionClass actionObj)
    {
        actionMap.put( key, actionObj );
    }

    public ActionClass getAction(String key)
    {
        return actionMap.get( key );
    }
}
