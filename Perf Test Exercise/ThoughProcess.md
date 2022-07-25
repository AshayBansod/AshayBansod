# Performance Suite Considerations

1. Considering requiremnt of load testing for very small load, jmeter was comfortable/more suitable else gatling was thought of to be used as well.
2. As it is performance verification/benchmarking, functional checks are not much considered barring basic one like response codes. 
3. To make reliable and/or sturdy, data persistence is not assumed and get, update, delete CRUD operations are being run only if create operation is successful. 
   Suite can be enhanced to create data if create request fails but that is further enhancement planned in next increment.
4. CSV dataset config files need to be placed on machine where test are running and location needs to be updated in suite

# Improvement Avenues
1. Summary representation can be done with powerBi dashboards, even current excel charts can be modified/improved for better representation. 
2. If real time analysis is required (which doesnt seem to be the case, as this is not streaming service/product), then influxDb and grafana can be integrated to jmx suite. 





