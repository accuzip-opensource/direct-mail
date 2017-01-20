<?php
/**
 * The Front Controller for handling every request
 *
 * CakePHP(tm) : Rapid Development Framework (http://cakephp.org)
 * Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 *
 * Licensed under The MIT License
 * For full copyright and license information, please see the LICENSE.txt
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright (c) Cake Software Foundation, Inc. (http://cakefoundation.org)
 * @link          http://cakephp.org CakePHP(tm) Project
 * @since         0.2.9
 * @license       MIT License (http://www.opensource.org/licenses/mit-license.php)
 */
// for built-in server
if (php_sapi_name() === 'cli-server') {
    $_SERVER['PHP_SELF'] = '/' . basename(__FILE__);

    $url = parse_url(urldecode($_SERVER['REQUEST_URI']));
    $file = __DIR__ . $url['path'];
    if (strpos($url['path'], '..') === false && strpos($url['path'], '.') !== false && is_file($file)) {
        return false;
    }
}
require dirname(__DIR__) . '/vendor/autoload.php';

use App\Application;
use Cake\Http\Server;
use Cake\Network\Http\Client;
// Bind your application to the server.
//$server = new Server(new Application(dirname(__DIR__) . '/config'));

// Run the request/response through the application
// and emit the response.
//$server->emit($server->run());

//Direct Mail POST upload file

use Cake\Network\Http\FormData;
	$http = new Client();
	$data = new FormData();
	
	$baseURL = 'http://cloud2.iaccutrace.com';	
	
	if( isset($_GET['apiKey']) && $_GET['apiKey'] )
	{
		$apiKey = $_GET['apiKey'];
	}
	else{
		throw new Exception('append apiKey parameter!');
	}
		
	// Create an XML part
	$backOfficeOption = $data->newPart( 'backOfficeOption', 'json' );
	$data->add( $backOfficeOption );

	$apiKey = $data->newPart( 'apiKey', $apiKey );
	$data->add( $apiKey );

	$callbackURL = $baseURL+'/callback.jsp';
	
	$callbackURL = $data->newPart( 'callbackURL', $callbackURL );
	$data->add( $callbackURL );

	$guid = $data->newPart('guid', '' );
	$data->add($guid);

	// Create a file upload with addFile()
	// This will append the file to the form data as well.
	$file = $data->addFile( 'file', fopen( dirname(__DIR__).'\sample.csv', 'r' ) );
	$data->add( $file );

	// Send the request.
	$uploadFileURL = $baseURL.'/ws_360_webapps/v2_0/uploadProcess.jsp?manual_submit=false';
	$response = $http->post(
		$uploadFileURL,
		(string)$data,		
		['headers' => ['Content-Type' => $data->contentType()]]
	);	

	$bodyVar = $response->body();
	echo $bodyVar;
	$bodyVarObj = json_decode( $bodyVar );
	echo ( $bodyVarObj->guid );
	
//	Direct Mail PUT update QUOTE
	$http = new Client();
	$quoteUpdate = '{
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
		"mailing_agent_crid": "8888888",
		"mailing_agent_edoc_sender_crid": "8888888",
		"prepared_for_name_address": "",
		"prepared_for_mailer_id": "999999",
		"prepared_for_crid": "8888888",
		"prepared_for_nonprofit_authorization_number": "",
		"permit_holder_name_address": "",
		"permit_holder_phone": "",
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
	}';
					
    $request = array(
        'header' => array(
            'Content-Type' => 'application/json',
        ),
    );					
		
	$updateQUOTEurl = $baseURL.'/servoy-service/rest_ws/ws_360/v2_0/job/'.$bodyVarObj->guid.'/QUOTE';
	$response = $http->put( $updateQUOTEurl, $quoteUpdate, $request );		

//Direct Mail GET QUOTE
	$getQUOTEurl = $baseURL.'/servoy-service/rest_ws/ws_360/v2_0/job/'.$bodyVarObj->guid.'/QUOTE';
	$http = new Client();
	$response = $http->get( $getQUOTEurl, [], [] );
//	echo $response->body();

//Direct Mail GET CASS-NCOA-DUPS_01-PRESORT
	$cassNcoaDups01presort = $baseURL.'/servoy-service/rest_ws/ws_360/v2_0/job/'.$bodyVarObj->guid.'/CASS-NCOA-DUPS_01-PRESORT';
	$http = new Client();
	$response = $http->get( $cassNcoaDups01presort, [], [] );
	echo $response->body();

	echo "DirectMail";