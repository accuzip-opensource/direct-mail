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

//Direct Mail POST
/*
use Cake\Network\Http\FormData;
	$http = new Client();
	$data = new FormData();

	// Create an XML part
	$backOfficeOption = $data->newPart( 'backOfficeOption', 'json' );
	$data->add( $backOfficeOption );

	$apiKey = $data->newPart( 'apiKey', '<YourDirectMailAPIkey>' );
	$data->add( $apiKey );

	$callbackURL = $data->newPart( 'callbackURL', '<YourWebHookCallback>' );
	$data->add( $callbackURL );

	$guid = $data->newPart('guid', '' );
	$data->add($guid);

	// Create a file upload with addFile()
	// This will append the file to the form data as well.
	$file = $data->addFile( 'file', fopen( 'C:/360/sample_2k.csv', 'r' ) );
	//$file->contentId('abc123');
	//$file->disposition('attachment');
	$data->add( $file );

	// Send the request.
	$response = $http->post(
		'<DirectMailURL>',
		(string)$data,
		
		['headers' => ['Content-Type' => $data->contentType()]]
	);	
*/

//Direct Mail GET
/*
	$http = new Client();
	$response = $http->get( '<DirectMailURL>', [], [] );
*/

//Direct Mail PUT
	$http = new Client();
	$quoteUpdate = '{
						"success": "true",
						"presort_class": "FIRST CLASS",
						"drop_zip": "93422",
						"mail_piece_size": "CARD"
					}';
					
    $request = array(
        'header' => array(
            'Content-Type' => 'application/json',
        ),
    );					
		
	$response = $http->put( '<DirectMailURL>', $quoteUpdate, $request );	

echo "DirectMail";
	echo $response->body();
