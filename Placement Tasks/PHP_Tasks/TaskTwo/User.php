<?php
class User
{

	#instance-variables
	private $userName;
    private $userAge;

    #constructors
    public function __construct($userName, $userAge)
    {
        $this->userName = $userName;
        $this->userAge = $userAge;
    }

    #functions
    public function get_userName()
    {
        return $this->userName;
    }
    public function set_userName($userName)
    {
        $this->userName = $userName;
    }
    public function get_userAge()
    {
        return $this->userAge;
    }
    public function set_userAge($userAge)
    {
        $this->userAge = $userAge;
    }

}#classUserEnds
?>