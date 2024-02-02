function dispLoading(msg){
    if( msg == undefined ){
        msg = "";
    }
    var dispMsg = "<div class='loadingMsg'><span class='loadImg'></span><span class='loadMsg'>" + msg + "</span></div>";
    if($("#loading").length == 0){
        $("body").append("<div id='loading'><div class='loadingBox'>" + dispMsg + "</div></div>");
    }
}
function removeLoading(){
    $("#loading").remove();
}