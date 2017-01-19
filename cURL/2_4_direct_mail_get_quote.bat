if [%1]==[] goto usage
curl -X GET -H "Content-Type: application/json" -H "Cache-Control: no-cache" "http://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/job/%1/QUOTE"
goto :eof

:usage
@echo Usage: python %0 CurrentGUID