# Framework Decisions

1. Test Framework is built considering to be at workflow/integration layer, not service level test suite. Scenarios are written with workflow approach. 
Hence, all tests are put in src/main as same codebase may be utilised for SLI probes in future considerations with minor tweaks.
2. Integration test lifecycle is decided to be managed using failsafe plugin as it will give granular control when pre and post integration phases will have to be added (if required) while scaling framework. As of now, test data clean-up is managed using tagged hooks feature of cucumber.  
3. Comments are intentionally avoided in codebase as whatever is written so far is (almost) self explanatory. 
4. BDD mechanism is chosen over Rest assured offered TDD approach intentionally so that feature files can be shared with or can come from portfolio owners when feasible.  
5. For reporting, cucumber's out of box reporting is being relied upon. Additional reporting (e.g. serenity) integration is not considered as integration tests will need to be coupled into CD/release pipeline. In CD pipeline, ADO dashboard reflects basic reporting. If additional reporting is at all required, HTML reporting plugins in CD/release pipelines can be one good option too.   
(This is very much subjective though process; to be discussed over call).
6. Framework is designed considering CD frequency will allow few minutes of test run time (no need for parallel execution). If parallel execution is intended, then minor modification to remove singleton request instance will serve purpose.  

# Considerations
1. Pet Id is changing after update. Test is written considering this is acceptable product behaviour
2. There is little deviation from standard Http status code practices. As of now test are not made to fail/follow strict http status code patterns for this as this is more of architectural decision. Test may be changed after a round of discussion.
3. Applications request response data types are not consistent, it is handled in test framework but can be done in more elegant manner
4. Response array has mix of long and integer, it is handled in test framework but can be done in more elegant manner.
5. createWithArray endpoint is having lot of latency but as of now, response time assertions haven't been put but can be considered in future after discussion.	
  

# Known Improvement Avenues
1. Lot of boiler plate code can be removed with lombok and/or builder.
2. Maven profile can be used as run configuration controller.
2. Response message assertion can be improved.
3. Unit test coverage needs to be improved and UT can be moved under src/test.
4. model package can be made more cleaner and segregated into separate request and response POJOs rather than all in one folder. 
5. After hooks could be moved to suitable packages but issues were faced during dependency injection. 
6. readme file needs to be created to mention - 'how to' for setup, run etc.

# Way forward
1. Considering this as workflow test suite, integration with CD to be considered. 
Test Step may be added in all non-prod stages and test can be triggered with verify goal of failsafe plugin - 'mvn verify'
2. Running this in pre-prod or prod can be considered (basically as good as synthetic SLI probes) with minor modifications. 




