<form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" enctype="multipart/form-data">
	<label for="file">Filename:</label> <input type="file" name="Filedata" id="Filedata" /> 
	<br />
	<input type="submit" name="submit" value="Submit" />
</form>

<?php
if ( $_POST[ 'submit' ] ) 
{    
    $RealTitleID = $_FILES['Filedata']['name'];
    
    $ch = curl_init( "<DirectMailURL>" );
    
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );
    curl_setopt( $ch, CURLOPT_POST, 1 );

    $args  = array(
    	   'backOfficeOption' => 'json',
    	   'apiKey' => '<YourDirectMailAPIkey>',
    	   'callbackURL' => '<YourWebHookCallback>',
    	   'guid' => '',
    	   'file' => new CurlFile( $_FILES['Filedata']['tmp_name'], 'file/exgpd', $RealTitleID )
    );    
    curl_setopt( $ch, CURLOPT_POSTFIELDS, $args );
    
    $result = curl_exec($ch);
    
    echo $result;
}
?>
