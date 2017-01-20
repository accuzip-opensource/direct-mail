# http POST

import sys
import requests
import urllib
import json

session = requests.Session()
file={'sample.csv':(open('..\sampleFiles\sample.csv','rb').read())}

if len(sys.argv)<2:
	print( "ERROR! Submit your api key as parameter!" );
	quit();
else:
	print( "DirectMail start..." );

baseURL = 'http://cloud2.iaccutrace.com';
	
apiKey = sys.argv[1];

url = baseURL+'/ws_360_webapps/v2_0/uploadProcess.jsp?manual_submit=false'
payload = {
	  'backOfficeOption'   :'json',     #json or mongo
	  'apiKey':apiKey, 
	  'callBack':baseURL+'/callback.jsp',
	  'guid':''
}

response = session.post( url, files=file, data=payload )
print(response.text)

uploadResponseJSON = json.loads(response.text)
print(uploadResponseJSON['guid'])

#update QUOTE
urlUpdateQUOTE = baseURL+'/servoy-service/rest_ws/ws_360/job/'+uploadResponseJSON['guid']+'/QUOTE'
payloadUpdateQOTE = {
		"success": "true",
		"presort_class": "FIRST CLASS",
		"drop_zip": "93422",
		"mail_piece_size": "CARD",
		"piece_height": "4.00",
		"piece_length": "5.00",
		"thickness_value": ".009",
		"thickness_based_on": "1",
		"tray_type": "MMM",
		"calculate_container_volume": "1",
		"min1ft": "",
		"max1ft": "",
		"min2ft": "",
		"max2ft": "",
		"print_barcode": "1",
		"print_imb": "1",
		"machinability": "MACHINABLE",
		"weight_value": ".2",
		"weight_unit": "OUNCES",
		"weight_based_on": "1",
		"mail_permit_type": "PROFIT",
		"mail_pay_method": "IMPRINT",
		"include_non_zip4": "1",
		"include_crrt": "0",
		"print_reverse": "0",
		"entry_scf": "0",
		"entry_ndc": "0",
		"agent_or_mailer_signing_statement": "",
		"agent_or_mailer_company": "",
		"agent_or_mailer_phone": "",
		"agent_or_mailer_email": "",
		"mailing_agent_name_address": "",
		"mailing_agent_phone": "",
		"mailing_agent_mailer_id": "999999",
		"mailing_agent_crid": "",
		"mailing_agent_edoc_sender_crid": "8888888",
		"prepared_for_name_address": "",
		"prepared_for_mailer_id": "999999",
		"prepared_for_crid": "8888888",
		"prepared_for_nonprofit_authorization_number": "",
		"permit_holder_name_address": "",
		"permit_holder_phone": "8054617300",
		"permit_holder_mailer_id": "999999",
		"permit_holder_crid": "8888888",
		"statement_number": "1",
		"mailing_date": "08/20/2014",
		"mail_permit_number": "199",
		"net_postage_due_permit_number": "",
		"postage_affixed": "",
		"exact_postage": "",
		"imb_default_mid": "999999",
		"imb_mid": "999999",
		"imb_starting_serial_number": "",
		"imb_service_type": "270",
		"maildat_pdr": "0",
		"maildat_mpu_name": "JOB1",
		"maildat_mpu_description": "TEST JOB",
		"accutrace_job_description": "TEST JOB",
		"accutrace_job_id": "123456",
		"accutrace_job_id2": "789",
		"accutrace_notice_email": "",
		"accutrace_customer_id": "",
		"accutrace_api_key": "",
		"format": "UPPER",
		"list_owner_paf_id": "E00001",
		"list_owner_information": "company|address|city|state|zip+4|telephone|naics|email|name|title|08/01/2014",
		"total_postage": "",
		"postage_saved": "",
		"First_Class_Card": "",
		"First_Class_Letter": "",
		"First_Class_Flat": "",
		"Standard_Card": "",
		"Standard_Letter": "",
		"Standard_Flat": "",
		"northsouth":"4"
}

response = session.put( urlUpdateQUOTE,  data=json.dumps( payloadUpdateQOTE ),  headers={ 'Content-Type':'application/json' } )
print( response.status_code ) 

#get QUOTE
urlGetQUOTE = baseURL+'/servoy-service/rest_ws/ws_360/job/'+uploadResponseJSON['guid']+'/QUOTE'
response = session.get( urlGetQUOTE )
print(response.text)

#get CASS-NCOA-DUPS_01-PRESORT
urlGetCass_Dups_01_Presort = baseURL+'/servoy-service/rest_ws/ws_360/job/'+uploadResponseJSON['guid']+'/CASS-NCOA-DUPS_01-PRESORT'
print(urlGetCass_Dups_01_Presort)
response = session.get( urlGetCass_Dups_01_Presort )
print(response.text)
