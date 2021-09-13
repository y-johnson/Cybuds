# Database schema for backend

### 9/10/2021 Johnson

The main issues that need to be addressed are essentially about complexity. The application has users but naturally
needs more than one archetype. Similarly, many elements of the platform involve storing information regarding people
such as profiles and imagery.

### OVERVIEW

* `User`
	* `user_id` [primary key]
		* Uniquely assigned by the database.
		* The `user_id` is useful for querying but is realistically not needed, assuming that `username` is also unique.
	* `username` [unique key]
		* User defined.
	* `email`
		* Could be made unique.
	* `password_hash`
		* We *really* shouldn't store the user's password in plaintext, so we could hash it and store that.
	* `first_name`
	* `middle_name`
	* `last_name`
	* `address`
	* `phone_number`
	* `user_img`

* `Moderator` [inherits from `User`]
	* `mod_id`
		* Since moderators inherit users but are distinct enough from them, they benefit from having their own ID.
	* `priviledges`
		* Moderators are not intended to be administrators and should not have control over everything so limiting what
		  administrative actions they may take is probably for the best.

### 9/13/2021 Johnson

`Users`

|user_id|username|email|password_hash|f_name|m_name|l_name|address|phone_number|user_img|
|---|--------|-----|-------------|------|------|------|-------|------------|--------|
|001|yjohnson|yjohnson@iastate.edu|some_hashed_pass|Yadiel|null|Johnson|Frederiksen Ct 53/515*******/some_blob|

`Moderators`

|mod_id|user_id|priviledges|
|---|---|---|
|101|001|`ALL`|
