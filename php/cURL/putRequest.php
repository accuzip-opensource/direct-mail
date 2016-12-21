<?php
	$curl = curl_init();
	$url = "<DirectMailURL>";
		
	$quoteUpdate = array(
		"success" => "true",
		"presort_class" => "FIRST CLASS",
		"total_postage" => "$659.41",
		"postage_saved" => "$890.90",
		"total_presort_records" => "3334",
		"mailing_agent_crid" => "5322168"
	);	
		
	$preparedData = json_encode( $quoteUpdate );
		
	echo $preparedData;	
		
	curl_setopt( $curl, CURLOPT_URL, $url );				
	curl_setopt( $curl, CURLOPT_CUSTOMREQUEST, "PUT" );
	curl_setopt( $curl, CURLOPT_HEADER, false );
	curl_setopt( $curl, CURLOPT_RETURNTRANSFER, true );
	curl_setopt( $curl, CURLOPT_HTTPHEADER, array( 'Content-Type: application/json' ) );
	curl_setopt( $curl, CURLOPT_POSTFIELDS, $preparedData );

	// Make the REST call, returning the result
	$response = curl_exec( $curl );
	echo $response;
/*	if( !$response ) 
	{
		die( "Connection Failure.n" );
	}*/					
?>
