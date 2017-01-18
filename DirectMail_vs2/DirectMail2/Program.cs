using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using Newtonsoft.Json.Linq;
using System.Threading;

namespace DirectMail2
{
    class Program
    {
        static void Main(string[] args)
        {
            string response = "";

            AccuZipDirectMailCsharpClientExample azdm = new AccuZipDirectMailCsharpClientExample("your api key");

            /*
             * Upload file
             */            
            FileInfo fi = new FileInfo("sample_2k.csv");
            string guid = azdm.upLoadFile(fi);


            /*
            * get quote
            */
            response = azdm.getQuote(guid);



            /*
            * update quote
            */
            response = azdm.updateQuote(guid);



            /*
            * run CASS DUPS_01 and PRESORT
            */
            response = azdm.runCass_Dups_01_Presort(guid);


            /*
            * get quote - (status of CASS DUPS_01 and PRESORT)
            */
            response = azdm.getQuote(guid);

          string success = JToken.Parse(response).SelectToken("success").ToString();
          if (success == null || !success.ToLower().Equals("true"))
          {
                Console.Out.WriteLine("response => " + response);
                return;
            }

            string task = "";
            if(JToken.Parse(response).SelectToken("task_name") != null){
                task = JToken.Parse(response).SelectToken("task_name").ToString();
            }

            string percent_completed = "0";
            if (JToken.Parse(response).SelectToken("task_percentage_completed") != null)
            {
                percent_completed = JToken.Parse(response).SelectToken("task_percentage_completed").ToString();
            }

            int time_count = 0;
            while(!percent_completed.Equals("100")){
                response = azdm.getQuote(guid);

                success = JToken.Parse(response).SelectToken("success").ToString();
                if (success == null || !success.ToLower().Equals("true"))
                {
                    Console.Out.WriteLine("response => " + response);
                    return;
                }

                if (JToken.Parse(response).SelectToken("task_name") != null)
                {
                    task = JToken.Parse(response).SelectToken("task_name").ToString();
                }

                if (JToken.Parse(response).SelectToken("task_percentage_completed") != null)
                {
                    percent_completed = JToken.Parse(response).SelectToken("task_percentage_completed").ToString();
                }

                Console.Out.WriteLine("task:" + task);
                Thread.Sleep(20000);
                time_count = time_count + 20000;

                
                if(time_count > 300000){
                    throw new Exception("process may be stalled");
                }

            }
           

            FileInfo fileout = new FileInfo("prev_presort.csv");
            response = azdm.downLoadPreviewCSV(guid, fileout);

            Console.Out.WriteLine("response => " + response);
            Console.Out.WriteLine("Done");
        }
    }
}
