# http GET

import requests
import urllib

session = requests.Session()
url = '<DirectMailURL>'
response = session.get( url )
print(response.text)
