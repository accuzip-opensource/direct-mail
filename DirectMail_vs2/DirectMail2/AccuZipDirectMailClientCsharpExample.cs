using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using RestSharp;
using Newtonsoft;


/**
 * AccuZip Direct Mail rest web service C# example.<br>
 * <br>
 * web service calls<br>
 * upLoadFile(FileInfo input_file) - https://speca.io/accuzip/accuzip-360#upload-file<br>
 * getQuote(string guid) - https://speca.io/accuzip/accuzip-360#get-quote<br>
 * updateQuote(string guid) -  https://speca.io/accuzip/accuzip-360#update-quot<br>
 * runCass_Dups_01_Presort(string guid) - https://speca.io/accuzip/accuzip-360#casscert-ncoalink-duplicatedetection-presort<br>
 * <br>
 * dependencies<br>
 * <br>
 * <<<<<<<<<<< restsharp = > http://restsharp.org/ >>>>>>>>>>>>>>>><br>
 * <br>
 * <<<<<<<<<<< Newtonsoft.Json => http://www.newtonsoft.com/json >>>>>>>>>>>>><br>
 */
public class AccuZipDirectMailCsharpClientExample
{
    /**
     * Your API KEY
     */
   public   string API_KEY = "your api key";    
   
    public AccuZipDirectMailCsharpClientExample(string api_key)
    {
        this.API_KEY = api_key;
    }

    /**
     * https://cloud2.iaccutrace.com/ws_360_webapps/{{ver}}/uploadProcess.jsp?manual_submit=false<br>
     * more documentation = >  https://speca.io/accuzip/accuzip-360#upload-file<br>
     * <br>
     * example response => {"success360Import":"true","quote_started":"true","cass_started":"false","guid":"e2814511-c7be-4711-b196-54f40fe7c361"}<br>
     * return String guid<br>
     */
    public String upLoadFile(FileInfo input_file)
    {
        string result = "";

        string url = "https://cloud2.iaccutrace.com/ws_360_webapps/v2_0/uploadProcess.jsp?manual_submit=false";
        RestClient rc = new RestClient();
        FileStream fs = null;

        try
        {
            if (this.API_KEY == null || this.API_KEY.Length == 0 ||
                this.API_KEY.Equals("your api key"))
            {
                throw new Exception("Api Key required. The Api Key currently is => " + this.API_KEY);
            }

            if (input_file.Exists)
            {
                rc.BaseUrl = new Uri(url);
                IRestRequest request = new RestRequest(Method.POST);
                request.AlwaysMultipartFormData = true;

                request.AddHeader("content-type", "multipart/form-data");

                request.AddParameter("backOfficeOption", "json");
                request.AddParameter("apiKey", this.API_KEY);
                request.AddParameter("callbackURL", "https://cloud2.iaccutrace.com/360_callBack_web_hook/callBack.jsp");
                request.AddParameter("guid", "");


                fs = input_file.OpenRead();
                byte[] buffer = null;

                {
                    buffer = new byte[fs.Length];
                    fs.Read(buffer, 0, (int)fs.Length);
                }


                request.AddFile("file", buffer, "sample_2k.csv", "text/plain");

                IRestResponse response = rc.Execute(request);

                if (response.Content != null && response.Content.Contains("html"))
                {
                    result = response.Content;
                    throw new Exception("error => " + result);
                }

                result = response.Content;

            }
            else
            {
                throw new Exception("File not found => " + input_file.FullName);
            }

        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (fs != null)
            {
                fs.Close();
                fs.Dispose();
            }
        }

        Newtonsoft.Json.Linq.JToken jtoken = Newtonsoft.Json.Linq.JToken.Parse(result).SelectToken("guid");

        return jtoken.ToString();
    }

    /**
     * https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/v2_0/job/{guid}/QUOTE<br>
     * <br>
     * quote example json response  (after upLoad file) =><br>
           {<br>
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
     * more documentation = > https://speca.io/accuzip/accuzip-360#get-quote<br>
     * return String JSON<br>
     */
    public string getQuote(string guid)
    {
        string result = "";
        string url_base = "https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/v2_0/job/";

        RestClient rc = new RestClient();

        StringBuilder sb = new StringBuilder();
        sb.Append(url_base);
        sb.Append(guid);
        sb.Append("/QUOTE");
        string url = sb.ToString();


        try
        {

            rc.BaseUrl = new Uri(url);
            RestRequest request = new RestRequest(Method.GET);
            IRestResponse response = rc.Execute(request);

            result = response.Content;

        }
        catch (Exception e)
        {
            throw e;
        }

        return result;

    }

    /**
     *  update quote - send presort parameters<br>
     *  https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/job/{guid}/QUOTE<br>
     *  more documentation = >  https://speca.io/accuzip/accuzip-360#update-quote<br>
     *  return String<br>
     */
    public string updateQuote(string guid)
    {
        string result = "";

        StringBuilder sb = new StringBuilder();
        string url_base = "https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/v2_0/job/";

        sb.Append(url_base);
        sb.Append(guid);
        sb.Append("/QUOTE");

        string url = sb.ToString();

        RestClient rc = new RestClient();

        try
        {
            rc.BaseUrl = new Uri(url);
            RestRequest request = new RestRequest(Method.PUT);
            request.AddHeader("Accept", "application/json");
            request.AddParameter("application/json", buildPresortParameters(), ParameterType.RequestBody);
            IRestResponse response = rc.Execute(request);

            result = response.Content;

        }
        catch (Exception e)
        {
            throw e;
        }
        return result;
    }


    /**
     * https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/job/{guid}/CASS-NCOA-DUPS_01-PRESORT<br>
     * example response => {"success": true}<br>
     * more documentation = > https://speca.io/accuzip/accuzip-360#casscert-ncoalink-duplicatedetection-presort<br>
     * return String JSON<br>
     */
    public string runCass_Dups_01_Presort(string guid)
    {
        string result = "";

        StringBuilder sb = new StringBuilder();
        string url_base = "https://cloud2.iaccutrace.com/servoy-service/rest_ws/ws_360/v2_0/job/";

        sb.Append(url_base);
        sb.Append(guid);
        sb.Append("/CASS-NCOA-DUPS_01-PRESORT");

        string url = sb.ToString();
        RestClient rc = new RestClient();
        try
        {
            rc.BaseUrl = new Uri(url);
            RestRequest request = new RestRequest(Method.GET);
            IRestResponse response = rc.Execute(request);

            result = response.Content;

        }
        catch (Exception e)
        {
            throw e;
        }

        return result;
    }

    /**
     * Example of populating the Presort parameters for a mailing. Used with updateQuote<br>
     * <br>
     * @return String JSON string <br>
     */

    public string buildPresortParameters()
    {
        string json_string = "";

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

        Newtonsoft.Json.Linq.JObject jsonObj = new Newtonsoft.Json.Linq.JObject();
        jsonObj.Add("success", "true");
        jsonObj.Add("presort_class", "STANDARD MAIL");
        jsonObj.Add("drop_zip", "93422");
        jsonObj.Add("mail_piece_size", "LETTER");
        jsonObj.Add("piece_height", "4.00");
        jsonObj.Add("piece_length", "5.00");
        jsonObj.Add("thickness_value", ".009");
        jsonObj.Add("thickness_based_on", "1");
        jsonObj.Add("tray_type", "MMM");
        jsonObj.Add("calculate_container_volume", "1");
        jsonObj.Add("min1ft", "");
        jsonObj.Add("max1ft", "");
        jsonObj.Add("min2ft", "");
        jsonObj.Add("max2ft", "");
        jsonObj.Add("print_barcode", "1");
        jsonObj.Add("print_imb", "1");
        jsonObj.Add("machinability", "NONMACHINABLE");
        jsonObj.Add("weight_value", ".2");
        jsonObj.Add("weight_unit", "OUNCES");
        jsonObj.Add("weight_based_on", "1");
        jsonObj.Add("mail_permit_type", "PROFIT");
        jsonObj.Add("mail_pay_method", "IMPRINT");
        jsonObj.Add("include_non_zip4", "1");
        jsonObj.Add("include_crrt", "0");
        jsonObj.Add("print_reverse", "0");
        jsonObj.Add("entry_scf", "0");
        jsonObj.Add("entry_ndc", "0");
        jsonObj.Add("agent_or_mailer_signing_statement", "STEVE BELMONTE");
        jsonObj.Add("agent_or_mailer_company", "ACCUZIP INC.");
        jsonObj.Add("agent_or_mailer_phone", "8054617300");
        jsonObj.Add("agent_or_mailer_email", "steve@accuzip.com");
        jsonObj.Add("mailing_agent_name_address", "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500");
        jsonObj.Add("mailing_agent_phone", "8054617300");
        jsonObj.Add("mailing_agent_mailer_id", "999999");
        jsonObj.Add("mailing_agent_crid", "8888888");
        jsonObj.Add("mailing_agent_edoc_sender_crid", "8888888");
        jsonObj.Add("prepared_for_name_address", "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500");
        jsonObj.Add("prepared_for_mailer_id", "999999");
        jsonObj.Add("prepared_for_crid", "8888888");
        jsonObj.Add("prepared_for_nonprofit_authorization_number", "");
        jsonObj.Add("permit_holder_name_address", "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500");
        jsonObj.Add("permit_holder_phone", "8054617300");
        jsonObj.Add("permit_holder_mailer_id", "999999");
        jsonObj.Add("permit_holder_crid", "8888888");
        jsonObj.Add("statement_number", "1");
        jsonObj.Add("mailing_date", "08/20/2014");
        jsonObj.Add("mail_permit_number", "199");
        jsonObj.Add("net_postage_due_permit_number", "");
        jsonObj.Add("postage_affixed", "");
        jsonObj.Add("exact_postage", "");
        jsonObj.Add("imb_default_mid", "999999");
        jsonObj.Add("imb_mid", "999999");
        jsonObj.Add("imb_starting_serial_number", "");
        jsonObj.Add("imb_service_type", "270");
        jsonObj.Add("json_maildat_pdr", "0");
        jsonObj.Add("json_maildat_mpu_name", "JOB1");
        jsonObj.Add("json_maildat_mpu_description", "TEST JOB");
        jsonObj.Add("json_accutrace_job_description", "TEST JOB");
        jsonObj.Add("json_accutrace_job_id", "123456");
        jsonObj.Add("json_accutrace_job_id2", "789");
        jsonObj.Add("json_accutrace_notice_email", "steve@accuzip.com");
        jsonObj.Add("json_accutrace_customer_id", "7700000101");
        jsonObj.Add("json_accutrace_api_key", "8B5A8632-31FC-4DA7-BDB9-D8B88897AF96");
        jsonObj.Add("format", "UPPER");
        jsonObj.Add("json_list_owner_paf_id", "E00001");
        jsonObj.Add("json_list_owner_information", "company|address|city|state|zip+4|telephone|naics|email|name|title|08/01/2014");
        jsonObj.Add("total_postage", "");
        jsonObj.Add("postage_saved", "");
        jsonObj.Add("First_Class_Card", "");
        jsonObj.Add("First_Class_Letter", "");
        jsonObj.Add("First_Class_Flat", "");
        jsonObj.Add("Standard_Card", "");
        jsonObj.Add("Standard_Letter", "");
        jsonObj.Add("Standard_Flat", "");

        json_string = jsonObj.ToString();

        return json_string;
    }


}

