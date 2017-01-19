# http POST

import requests
import sys

r = requests.get( "http://cloud2.iaccutrace.com/ws_360_webapps/v2_0/download.jsp?guid="+sys.argv[1]+"&ftype=prev.csv" )
with open("prev.csv", "wb") as code:
	code.write(r.content)