# http PUT

import requests
import urllib
import json

session = requests.Session()
url = '<DirectMailURL>'
payload = { 'mailing_agent_crid':'5322168','presort_class':'FIRST CLASS'}

response = session.put( url,  data=json.dumps( payload ),  headers={ 'Content-Type':'application/json' } )
print( response.status_code ) 
