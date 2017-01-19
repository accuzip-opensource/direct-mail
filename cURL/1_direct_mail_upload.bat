if [%1]==[] goto usage
curl -X POST -H "Cache-Control: no-cache" -H "Content-Type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW" -F "backOfficeOption=json" -F "apiKey=%1" -F "callbackURL=http://cloud2.iaccutrace.com/callback.jsp" -F "guid=" -F "file=@..\sampleFiles\sample.csv" "http://cloud2.iaccutrace.com/ws_360_webapps/uploadProcess.jsp?manual_submit=false"
goto :eof

:usage
@echo Usage: python %0 YourApiKey