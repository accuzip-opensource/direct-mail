if [%1]==[] goto usage
curl -o prev.csv "http://cloud2.iaccutrace.com/ws_360_webapps/v2_0/download.jsp?guid=%1&ftype=prev.csv"
goto :eof

:usage
@echo Usage: python %0 CurrentGUID