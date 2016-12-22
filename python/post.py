# http POST

import requests
import urllib

session = requests.Session()
file={'sample.csv':(open('sample.csv','rb').read())}
url = '<DirectMailURL>'
payload = {
  'backOfficeOption'   :'json',     #json or mongo
  'apiKey':'<YourDirectMailAPIkey>', 
  'callBack':'<YourWebHookCallback>',
  'guid':''
}

response = session.post( url, files=file, data=payload )
print(response.text)
