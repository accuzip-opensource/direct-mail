package rest1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import com.sun.jersey.multipart.impl.MultiPartWriter;

/**
 * AccuZip Direct Mail rest web service java example.<br>
 * <br>
 * web service calls<br>
 * upLoadFile(File input_file) - https://speca.io/accuzip/accuzip-360#upload-file<br>
 * getQuote(String guid) - https://speca.io/accuzip/accuzip-360#get-quote<br>
 * updateQuote(String guid) -  https://speca.io/accuzip/accuzip-360#update-quot<br>
 * runCass_Dups_01_Presort(String guid) - https://speca.io/accuzip/accuzip-360#casscert-ncoalink-duplicatedetection-presort<br>
 * <br>
 * <br>
 * dependencies<br>
 * <br>
 * <<<<< Jersey RESTful Web Services framework >>>>>><br>
 * https://jersey.java.net/<br>
 * jersey-bundle-1.19.jar =>  https://jersey.java.net/download.html<br>
 * jersey-multipart-1.13.jar => https://jersey.java.net/documentation/latest/modules-and-dependencies.html<br>
 * https://mvnrepository.com/artifact/com.sun.jersey.contribs/jersey-multipart/1.19<br>
 * <br>
 * <<<<< Java toolkit for JSON >>>>>>>>>>>>><br>
 * https://code.google.com/archive/p/json-simple/downloads<br>
 *<br>
 */
public class AccuZipDirectMailJavaClientExample {
	
	 final  String url_base = "https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/v2_0/job/";
	 

	/**
	 * your API KEY
	 */
	public String api_key = "your api_key";	

	private Client client = null;

	public AccuZipDirectMailJavaClientExample(String api_key) throws Exception{
		this.api_key = api_key;
		initClient();
	}

	private void initClient() throws Exception{

		try{
			ClientConfig config = new DefaultClientConfig();
			config.getClasses().add(MultiPartWriter.class);
			this.client = Client.create(config);

		}catch(Exception e){
			throw e;
		}
	}

	/**
	 * 
	 * https://cloud2.iaccutrace.com/ws_360_webapps/{{ver}}/uploadProcess.jsp?manual_submit=false<br>
	 * 
	 *  more documentation = >  https://speca.io/accuzip/accuzip-360#upload-file<br>
	 * 
	 * @return String guid<br>
	 * @throws Exception 
	 */
	public String upLoadFile(File input_file) throws Exception{
				
		
		String guid = "";
		try{
			
			if(this.api_key == null || this.api_key.trim().length() == 0
					|| this.api_key.equals("your api_key")){
				throw new Exception("Bad API_KEY. Current value of API_KEY is:" + this.api_key);
			}else if(input_file == null || !input_file.exists()){
				throw new Exception("Bad input file. Current input file is:" + input_file);
			}
			
			String url = "https://cloud2.iaccutrace.com/ws_360_webapps/v2_0/uploadProcess.jsp?manual_submit=false";

			WebResource webResource = client.resource(url);

			FormDataMultiPart formDataMultiPart = new FormDataMultiPart();

			formDataMultiPart.field("backOfficeOption", "json",  MediaType.TEXT_PLAIN_TYPE);
			formDataMultiPart.field("apiKey", this.api_key,  MediaType.TEXT_PLAIN_TYPE);
			formDataMultiPart.field("callbackURL", "https://cloud2.iaccutrace.com/360_callBack_web_hook/callBack.jsp",  MediaType.TEXT_PLAIN_TYPE);
			formDataMultiPart.field("guid", "",  MediaType.TEXT_PLAIN_TYPE);
			
			FileDataBodyPart fileDataBodyPart1 = new FileDataBodyPart("file", input_file, MediaType.APPLICATION_OCTET_STREAM_TYPE);
			formDataMultiPart.bodyPart(fileDataBodyPart1);

			ClientResponse clientResponse = webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
			.accept("*/*")
			.post(ClientResponse.class, formDataMultiPart);

			String result = clientResponse.getEntity(String.class);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result);
			JSONObject jsonObject = (JSONObject)obj;
			String success360Import = jsonObject.get("success360Import").toString();
			 guid = jsonObject.get("guid").toString();
			
			System.out.println("success360Import => " + success360Import);
			System.out.println("guid => " + guid);
			
			

		}catch(Exception e){
			
			throw e;
		}
		return guid;
	}
	
	/**
	 * https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/v2_0/job/{guid}/QUOTE<br>
	 * 
	 * <br>
	 * quote json response =><br>
	 *          {<br>
				  "First_Class_Flat": "$619",<br>
				  "Estimated_Postage_Standard_Card": "$548",<br>
				  "Estimated_Postage_Standard_Letter": "$548",<br>
				  "format": "",<br>
				  "First_Class_Card": "$154",<br>
				  "Estimated_Postage_First_Class_Card": "$526",<br>
				  "drop_zip": "",<br>
				  "First_Class_Letter": "$132",<br>
				  "total_presort_records": "",<br>
				  "Estimated_Postage_Standard_Flat": "$935",<br>
				  "Standard_Card": "$382",<br>
				  "total_postage": "",<br>
				  "Standard_Letter": "$382",<br>
				  "Estimated_Postage_First_Class_Flat": "$1,261",<br>
				  "success": true,<br>
				  "presort_class": "",<br>
				  "total_records": "2,000",<br>
				  "Estimated_Postage_First_Class_Letter": "$798",<br>
				  "mail_piece_size": "",<br>
				  "Standard_Flat": "$945",<br>
				  "postage_saved": ""<br>
				}<br>
	 * <br>
	 * 
	 * more documentation = > https://speca.io/accuzip/accuzip-360#get-quote<br>
	 * @return String JSON<br>
	 * @throws Exception 
	 */
	public String getQuote(String guid) throws Exception{
		
		String result = "";
		StringBuffer sb = new StringBuffer();

		try{
			sb.append(this.url_base);
			sb.append(guid);
			sb.append("/QUOTE");
			String url = sb.toString();
			
			WebResource webResource = client.resource(url);
			result = webResource.type("*/*").accept("*/*").get(String.class);
			
		}catch(Exception e){
			
			throw e;
		}
		return result;
	}

	/**
	 *  update quote - send presort parameters<br>
	 *  https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/job/{guid}/QUOTE<br>
	 *  
	 *  more documentation = >  https://speca.io/accuzip/accuzip-360#update-quote<br>
	 *  @return String
	 * @throws Exception 
	 */
	public String updateQuote(String guid) throws Exception{
				
				String response = "";
				StringBuffer sb = new StringBuffer();				
						
				sb.append(this.url_base);
				sb.append(guid);
				sb.append("/QUOTE");
				
				String url = sb.toString();	
				
				try{
					
					WebResource webResource = client.resource(url);
					response = webResource.type("*/*").accept("*/*").put(String.class, buildPresortParameters());
					
				}catch(Exception e){
					
					throw e;
				}
				
				return response;
			}
			
	
			/**
			 * https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/job/{guid}/CASS-NCOA-DUPS_01-PRESORT<br>
			 * 
			 *  more documentation = > https://speca.io/accuzip/accuzip-360#casscert-ncoalink-duplicatedetection-presort<br>
			 * 
			 * @param guid
			 * @return String JSON
			 * @throws Exception 
			 */
			public String  runCass_Dups_01_Presort(String guid) throws Exception{
				String response = "";
				
				StringBuffer sb = new StringBuffer();				
						
				sb.append(this.url_base);
				sb.append(guid);
				sb.append("/CASS-NCOA-DUPS_01-PRESORT");
				
				String url = sb.toString();	
				
				try{
					
					WebResource webResource = client.resource(url);
					response = webResource.type("*/*").accept("*/*").get(String.class);
					
				}catch(Exception e){
					
					throw e;
				}
				
				return response;
				
			}
			
			/**
			 * 
			 * more documentation => https://speca.io/accuzip/accuzip-360#download-print-ready-presorted-csv<br>
			 * 
			 * @param guid
			 * @return
			 * @throws Exception
			 */
			public String downLoadPreviewCSV(String guid, File fileout) throws Exception{
				String response = "";
						
				String url = "https://cloud2.iaccutrace.com/ws_360_webapps/v2_0/download.jsp?guid=" + guid + "&ftype=prev.csv";
								
				InputStream is = null;
				BufferedInputStream bis = null;
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				
				try{
					WebResource webResource = client.resource(url);

					/**
					 * write file
					 * 
					 */
					is = webResource.type("*/*").accept("*/*").get(InputStream.class);
					byte[] data = new byte[4096];
					bis = new BufferedInputStream(is);
					fos = new FileOutputStream(fileout);
					bos = new BufferedOutputStream(fos);
					int count;
					while ((count = bis.read(data, 0, 4096)) != -1) {
						bos.write(data, 0, count);
					}

					bos.flush();
					bos.close();
					response = "Download Presort CSV to => " + fileout.getAbsolutePath();
					
				}catch(Exception e){
					throw e;
				}finally{
					if (is != null) {
						is.close();
					}
					if (bis != null) {
						bis.close();
					}
					if (fos != null) {
						fos.close();
					}
				}
				
				
			  return response;	
			}
			
			
			/**
			 * Example of populating the Presort parameters for a mailing. Used with updateQuote<br>
			 * <br>
			 * @return String JSON 
			 */
			@SuppressWarnings("unchecked")
			private String buildPresortParameters(){
				String json_string = "";
				JSONObject jsonObj = new JSONObject();
				
				/* 
				 * Presort parameters example
				    {
						"success": "true",
						"presort_class": "STANDARD MAIL",
						"drop_zip": "93422",
						"mail_piece_size": "LETTER",
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
						"machinability": "NONMACHINABLE",
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
						"agent_or_mailer_signing_statement": "STEVE BELMONTE",
						"agent_or_mailer_company": "ACCUZIP INC.",
						"agent_or_mailer_phone": "8054617300",
						"agent_or_mailer_email": "steve@accuzip.com",
						"mailing_agent_name_address": "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500",
						"mailing_agent_phone": "8054617300",
						"mailing_agent_mailer_id": "999999",
						"mailing_agent_crid": "8888888",
						"mailing_agent_edoc_sender_crid": "8888888",
						"prepared_for_name_address": "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500",
						"prepared_for_mailer_id": "999999",
						"prepared_for_crid": "8888888",
						"prepared_for_nonprofit_authorization_number": "",
						"permit_holder_name_address": "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500",
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
						"json_maildat_pdr": "0",
						"json_maildat_mpu_name": "JOB1",
						"json_maildat_mpu_description": "TEST JOB",
						"json_accutrace_job_description": "TEST JOB",
						"json_accutrace_job_id": "123456",
						"json_accutrace_job_id2": "789",
						"json_accutrace_notice_email": "steve@accuzip.com",
						"json_accutrace_customer_id": "7700000101",
						"json_accutrace_api_key": "8B5A8632-31FC-4DA7-BDB9-D8B88897AF96",
						"format": "UPPER",
						"json_list_owner_paf_id": "E00001",
						"json_list_owner_information": "company|address|city|state|zip+4|telephone|naics|email|name|title|08/01/2014",
						"total_postage": "",
						"postage_saved": "",
						"First_Class_Card": "",
						"First_Class_Letter": "",
						"First_Class_Flat": "",
						"Standard_Card": "",
						"Standard_Letter": "",
						"Standard_Flat": ""
					 }
				*/
				
				
				jsonObj.put("success", "true");
				jsonObj.put("presort_class", "STANDARD MAIL");
				jsonObj.put("drop_zip", "93422");
				jsonObj.put("mail_piece_size", "LETTER");
				jsonObj.put("piece_height", "4.00");
				jsonObj.put("piece_length", "5.00");
				jsonObj.put("thickness_value", ".009");
				jsonObj.put("thickness_based_on", "1");
				jsonObj.put("tray_type", "MMM");
				jsonObj.put("calculate_container_volume", "1");
				jsonObj.put("min1ft", "");
				jsonObj.put("max1ft", "");
				jsonObj.put("min2ft", "");
				jsonObj.put("max2ft", "");
				jsonObj.put("print_barcode", "1");
				jsonObj.put("print_imb", "1");
				jsonObj.put("machinability", "NONMACHINABLE");
				jsonObj.put("weight_value", ".2");
				jsonObj.put("weight_unit", "OUNCES");
				jsonObj.put("weight_based_on", "1");
				jsonObj.put("mail_permit_type", "PROFIT");
				jsonObj.put("mail_pay_method", "IMPRINT");
				jsonObj.put("include_non_zip4", "1");
				jsonObj.put("include_crrt", "0");
				jsonObj.put("print_reverse", "0");
				jsonObj.put("entry_scf", "0");
				jsonObj.put("entry_ndc", "0");
				jsonObj.put("agent_or_mailer_signing_statement", "STEVE BELMONTE");
				jsonObj.put("agent_or_mailer_company", "ACCUZIP INC.");
				jsonObj.put("agent_or_mailer_phone", "8054617300");
				jsonObj.put("agent_or_mailer_email", "steve@accuzip.com");
				jsonObj.put("mailing_agent_name_address", "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500");
				jsonObj.put("mailing_agent_phone", "8054617300");
				jsonObj.put("mailing_agent_mailer_id", "999999");
				jsonObj.put("mailing_agent_crid", "8888888");
				jsonObj.put("mailing_agent_edoc_sender_crid", "8888888");
				jsonObj.put("prepared_for_name_address", "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500");
				jsonObj.put("prepared_for_mailer_id", "999999");
				jsonObj.put("prepared_for_crid", "8888888");
				jsonObj.put("prepared_for_nonprofit_authorization_number", "");
				jsonObj.put("permit_holder_name_address", "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500");
				jsonObj.put("permit_holder_phone", "8054617300");
				jsonObj.put("permit_holder_mailer_id", "999999");
				jsonObj.put("permit_holder_crid", "8888888");
				jsonObj.put("statement_number", "1");
				jsonObj.put("mailing_date", "08/20/2014");
				jsonObj.put("mail_permit_number", "199");
				jsonObj.put("net_postage_due_permit_number", "");
				jsonObj.put("postage_affixed", "");
				jsonObj.put("exact_postage", "");
				jsonObj.put("imb_default_mid", "999999");
				jsonObj.put("imb_mid", "999999");
				jsonObj.put("imb_starting_serial_number", "");
				jsonObj.put("imb_service_type", "270");
				jsonObj.put("json_maildat_pdr", "0");
				jsonObj.put("json_maildat_mpu_name", "JOB1");
				jsonObj.put("json_maildat_mpu_description", "TEST JOB 1010");
				jsonObj.put("json_accutrace_job_description", "TEST JOB 1010");
				jsonObj.put("json_accutrace_job_id", "123456");
				jsonObj.put("json_accutrace_job_id2", "789");
				jsonObj.put("json_accutrace_notice_email", "steve@accuzip.com");
				jsonObj.put("json_accutrace_customer_id", "7700000101");
				jsonObj.put("json_accutrace_api_key", "8B5A8632-31FC-4DA7-BDB9-D8B88897AF96");
				jsonObj.put("format", "UPPER");
				jsonObj.put("json_list_owner_paf_id", "E00001");
				jsonObj.put("json_list_owner_information", "company|address|city|state|zip+4|telephone|naics|email|name|title|08/01/2014");
				jsonObj.put("total_postage", "");
				jsonObj.put("postage_saved", "");
				jsonObj.put("First_Class_Card", "");
				jsonObj.put("First_Class_Letter", "");
				jsonObj.put("First_Class_Flat", "");
				jsonObj.put("Standard_Card", "");
				jsonObj.put("Standard_Letter", "");
				jsonObj.put("Standard_Flat", "");
				
				json_string = jsonObj.toJSONString();
				return json_string;
			}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String response = null;
		try{
			/**
			 * your API KEY
			 */
			AccuZipDirectMailJavaClientExample ac = new AccuZipDirectMailJavaClientExample("your api key");

			File input = new File("sample_2k.csv");
			String guid = ac.upLoadFile(input);
			
			
			 response = ac.getQuote(guid);

			
			
		     response = ac.updateQuote(guid);
			
			
			  response = ac.runCass_Dups_01_Presort(guid);

			
			//check status
			 response = ac.getQuote(guid);
			 
			 	JSONParser parser = new JSONParser();
				Object obj = parser.parse(response);
				JSONObject jsonObject = (JSONObject)obj;
				String success = jsonObject.get("success").toString();
				
				if(success.equals("false")){
					 System.out.println("response => " + response);
					 return;					 
				 }
				
				String task = "";
				if(jsonObject.get("task_name") != null){
					task = jsonObject.get("task_name").toString();
				}
				
				String percent_completed = "0";
				if(task.equals("PRESORT")){
					percent_completed = jsonObject.get("task_percentage_completed").toString();
				}
				
				
			 
			while(!percent_completed.equals("100")){
				//check status
				 response = ac.getQuote(guid);
				 
//				JSONParser parser = new JSONParser();
				 obj = parser.parse(response);
				 jsonObject = (JSONObject)obj;
				 success = jsonObject.get("success").toString();
				 
				 if(success.equals("false")){
					 System.out.println("response => " + response);
					 return;					 
				 }
				 
				 if(jsonObject.get("task_name") != null){
						task = jsonObject.get("task_name").toString();
					}
				 
				 if(jsonObject.get("task_percentage_completed") != null){
						percent_completed = jsonObject.get("task_percentage_completed").toString();
					}
								
				 Thread.sleep(20000);
			 }
			
			 
			 File fileout = new File("prev_presort.csv");
			response = ac.downLoadPreviewCSV(guid, fileout);
			
			
			System.out.println("response => " + response);
			System.out.println("process finished");
			
			
		}catch(Exception e){
			
			throw e;
		}

	}

}
