package jp.happyhotel.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseAction
{

    public abstract void execute(HttpServletRequest request, HttpServletResponse response);

}
