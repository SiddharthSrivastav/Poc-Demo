POC includes two Apis with endpoints 
1. "/createReport" - For creating the new report with autogenrated reportId.
2. "/download" - For downloading excel file having multiple records based on date of creation or Report Id as filters.

This POC includes Swagger 3 and AOuth2 integration which has two type of user one Genaral USER and second ADMIN with specfic resource access permissions.
Spring boot version used : 3.3.1
Java version -17
DB Used - Mysql( for storing predefined Departments) AND MongoDB (for storing Reports)

