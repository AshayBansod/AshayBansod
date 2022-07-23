#Author: ashaybansod@outlook.com

Feature: Pet operations
  I want to use check working of pet operations

  @Sanity
  @cleanUpPets
  Scenario Outline: Verification of user CRUD operations
    Given I Create <pets>
    When I update <Details> of pets
    Then I should get updated pet data by <petState>
    
    Examples: 
      | pets  																	 | petState |               Details 				   	       |   
      | "/inputPayloads/createPetPayload.json"   | "sold"   | "/inputPayloads/updatePetPayload.json"   |
      | "/inputPayloads/createPetPayload2.json"  | "sold"   | "/inputPayloads/updatePetPayload2.json"  |
