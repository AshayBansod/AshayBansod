#Author: ashaybansod@outlook.com

Feature: User Operations
  I want to test user operations

  @Sanity1
  @cleanUpupdatedUsers
  Scenario Outline: Verification of user CRUD operations
    Given I Create single <user>
    When I update <user> with <userDetails>
    Then I should get updated user data
    
    Examples: 
      | user  																	|     user      |              userDetails 					   	 |   
      | "/inputPayloads/creatUserPayload.json"  |   "Trials1"   | "/inputPayloads/updateUserPayload.json"|

  @Sanity
  @cleanUpMultipleUsers
  Scenario Outline: Verification of mutliple user Create operations
    When I Create multiple <users>
    Then all users can be fetched successfully
    
    Examples: 
      | users  																	         |       
      | "/inputPayloads/creatMultipleUsersPayload.json"  |  