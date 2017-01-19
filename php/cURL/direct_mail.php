<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" enctype="multipart/form-data">
	<label for="file">FileName:</label> <input type="file" name="Filedata" id="Filedata" /> 
	<br />
	<label for="apiKey">Your API_KEY:</label><input type="text" name="apiKey" size="40">
	<input type="submit" name="submit" value="Submit" />
</form>

<?php
if ( isset($_POST[ 'submit' ]) && $_POST[ 'submit' ] ) 
{    
    $RealTitleID = $_FILES['Filedata']['name'];
    
	$baseURL = 'http://cloud2.iaccutrace.com';
	
	$apiKey = $_POST['apiKey'];
	if( isset($_POST['apiKey']) && $_POST['apiKey'] )
	{
		$apiKey = $_POST['apiKey'];
	}
	else{
		throw new Exception('submit apiKey field above!');
	}	
	
    $ch = curl_init( $baseURL."/ws_360_webapps/v2_0/uploadProcess.jsp?manual_submit=false" );
    
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch, CURLOPT_POST, 1 );

    $args  = array(
    	   'backOfficeOption' => 'json',
    	   'apiKey' => $apiKey,
    	   'callbackURL' => $baseURL.'/callback.jsp',
    	   'guid' => '',
    	   'file' => new CurlFile( $_FILES['Filedata']['tmp_name'], 'file/exgpd', $RealTitleID )
    );    
    curl_setopt( $ch, CURLOPT_POSTFIELDS, $args );
    
    $result = curl_exec($ch);
//    echo $result;
	$bodyVarObj = json_decode( $result );	
	
//	updateQUOTE
	$curl = curl_init();
	$url = $baseURL.'/servoy-service/rest_ws/ws_360/job/'.$bodyVarObj->guid.'/QUOTE';		
	$quoteUpdate = array(
		"success" => "true",
		"presort_class" => "FIRST CLASS",
		"drop_zip" => "93422",
		"mail_piece_size" => "CARD",
		"piece_height" => "4.00",
		"piece_length" => "5.00",
		"thickness_value" => ".009",
		"thickness_based_on" => "1",
		"tray_type" => "MMM",
		"calculate_container_volume" => "1",
		"min1ft" => "",
		"max1ft" => "",
		"min2ft" => "",
		"max2ft" => "",
		"print_barcode" => "1",
		"print_imb" => "1",
		"machinability" => "MACHINABLE",
		"weight_value" => ".2",
		"weight_unit" => "OUNCES",
		"weight_based_on" => "1",
		"mail_permit_type" => "PROFIT",
		"mail_pay_method" => "IMPRINT",
		"include_non_zip4" => "1",
		"include_crrt" => "0",
		"print_reverse" => "0",
		"entry_scf" => "0",
		"entry_ndc" => "0",
		"agent_or_mailer_signing_statement" => "STEVE BELMONTE",
		"agent_or_mailer_company" => "ACCUZIP INC.",
		"agent_or_mailer_phone" => "8054617300",
		"agent_or_mailer_email" => "steve@accuzip.com",
		"mailing_agent_name_address" => "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500",
		"mailing_agent_phone" => "8054617300",
		"mailing_agent_mailer_id" => "999999",
		"mailing_agent_crid" => "8888888",
		"mailing_agent_edoc_sender_crid" => "8888888",
		"prepared_for_name_address" => "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500",
		"prepared_for_mailer_id" => "999999",
		"prepared_for_crid" => "8888888",
		"prepared_for_nonprofit_authorization_number" => "",
		"permit_holder_name_address" => "Steve Belmonte|AccuZIP Inc.|3216 El Camino Real|Atascadero CA 93422-2500",
		"permit_holder_phone" => "8054617300",
		"permit_holder_mailer_id" => "999999",
		"permit_holder_crid" => "8888888",
		"statement_number" => "1",
		"mailing_date" => "08/20/2014",
		"mail_permit_number" => "199",
		"net_postage_due_permit_number" => "",
		"postage_affixed" => "",
		"exact_postage" => "",
		"imb_default_mid" => "999999",
		"imb_mid" => "999999",
		"imb_starting_serial_number" => "",
		"imb_service_type" => "270",
		"maildat_pdr" => "0",
		"maildat_mpu_name" => "JOB1",
		"maildat_mpu_description" => "TEST JOB",
		"accutrace_job_description" => "TEST JOB",
		"accutrace_job_id" => "123456",
		"accutrace_job_id2" => "789",
		"accutrace_notice_email" => "steve@accuzip.com",
		"accutrace_customer_id" => "7700000101",
		"accutrace_api_key" => "8B5A8632-31FC-4DA7-BDB9-D8B88897AF96",
		"format" => "UPPER",
		"list_owner_paf_id" => "E00001",
		"list_owner_information" => "company|address|city|state|zip+4|telephone|naics|email|name|title|08/01/2014",
		"total_postage" => "",
		"postage_saved" => "",
		"First_Class_Card" => "",
		"First_Class_Letter" => "",
		"First_Class_Flat" => "",
		"Standard_Card" => "",
		"Standard_Letter" => "",
		"Standard_Flat" => "",
		"northsouth" => "4"		
	);	
		
	$preparedData = json_encode( $quoteUpdate );
	curl_setopt( $curl, CURLOPT_URL, $url );				
	curl_setopt( $curl, CURLOPT_CUSTOMREQUEST, "PUT" );
	curl_setopt( $curl, CURLOPT_HEADER, false );
	curl_setopt( $curl, CURLOPT_RETURNTRANSFER, true );
	curl_setopt( $curl, CURLOPT_HTTPHEADER, array( 'Content-Type: application/json' ) );
	curl_setopt( $curl, CURLOPT_POSTFIELDS, $preparedData );

	// Make the REST call, returning the result
	$response = curl_exec( $curl );
//	echo $response;	

	
// 	get QUOTE	
	$service_url = $baseURL.'/servoy-service/rest_ws/ws_360/job/'.$bodyVarObj->guid.'/QUOTE';
	$curl = curl_init($service_url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	$curl_response = curl_exec($curl);
	if ($curl_response === false) 
	{
	    $info = curl_getinfo($curl);
	    curl_close($curl);
	    die('error occured during curl exec. Additioanl info: ' . var_export($info));
	}
	curl_close($curl);
//	echo $curl_response;

//	get CASS-NCOA-DUPS_01-PRESORT
	$service_url = $baseURL.'/servoy-service/rest_ws/ws_360/job/'.$bodyVarObj->guid.'/CASS-NCOA-DUPS_01-PRESORT';
	$curl = curl_init($service_url);
	curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	$curl_response = curl_exec($curl);
	if ($curl_response === false) 
	{
	    $info = curl_getinfo($curl);
	    curl_close($curl);
	    die('error occured during curl exec. Additioanl info: ' . var_export($info));
	}
	curl_close($curl);
	echo $curl_response;	
}
?>
