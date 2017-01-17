using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;

namespace DirectMail2
{
    class Program
    {
        static void Main(string[] args)
        {
            string response = "";

            AccuZipDirectMailCsharpClientExample azdm = new AccuZipDirectMailCsharpClientExample("your api_key");

            /*
             * Upload file
             */
            //FileInfo fi = new FileInfo("C:\\myDocs\\servoyDevelopmentStuff\\AZ_testing\\Cass postman\\Postman rest webservice stuff\\Proof of Concept Rest API Calls\\sample_2k.csv");
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

            Console.Out.WriteLine("Done");
        }
    }
}
