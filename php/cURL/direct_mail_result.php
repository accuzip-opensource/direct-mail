<?php
		
	$service_url = 'http://cloud2.iaccutrace.com/ws_360_webapps/v2_0/download.jsp?guid='.$_GET['guid'].'&ftype=prev.csv';
	download( $service_url );
	
function download( $url ) {
    set_time_limit(0);
    $ch = curl_init();
    curl_setopt($ch, CURLOPT_URL, $url);
    curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, 0);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
    $r = curl_exec($ch);
    curl_close($ch);
    header('Expires: 0'); // no cache
    header('Cache-Control: must-revalidate, post-check=0, pre-check=0');
    header('Last-Modified: ' . gmdate('D, d M Y H:i:s', time()) . ' GMT');
    header('Cache-Control: private', false);
    header('Content-Type: application/force-download');
    header('Content-Disposition: attachment; filename="prev.csv"');
    header('Content-Transfer-Encoding: binary');
    header('Content-Length: ' . strlen($r)); // provide file size
    header('Connection: close');
    echo $r;
}	
	
?>
