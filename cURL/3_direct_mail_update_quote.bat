if [%1]==[] goto usage
curl -X PUT -H "Content-Type: application/json" -H "Cache-Control: no-cache" --data-binary "@..\sampleFiles\json_values_example.json" "http://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/job/%1/QUOTE"
goto :eof

:usage
@echo Usage: python %0 CurrentGUID