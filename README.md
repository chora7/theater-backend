# API Authentication <!-- (OAuth 2.0) -->
- `Authorization: Bearer <token>`

# Anyone
- register (POST /auth/register/*?isAdmin=false|true*)
```
{
    "username": "",
    "password": ""
}
```
- login (POST /auth/login)
```
{
    "username": "",
    "password": ""
}
```
- logout (POST /auth/logout)

# ROLE_USER (__not available for testing__)
- view only projects assigned to them
- view only performances they are on
- view only departments assigned to them

# ROLE_ADMIN
- view all departments (GET /api/departments)
- add new department (POST /api/departments)
```
{
  "name": "department id",
  "userNames": []
}
```
- update existing department (PUT /api/departments/id)
```
{
  "name": "department id",
  "userNames": []
}
```
- delete existing department (DELETE /api/departments/id)
```
{
  "name": "department id",
  "userNames": []
}
```
- assign department to user (POST /api/departments/depid/assign/userid) __ids may be opposite__

- view all locations (GET /api/locations)
- add new location __not available__

- view all existing projects (GET /api/projects)
- view single project (GET /api/projects/id)
- add new project (POST /api/projects)
```
{
  "title": "",
  "description": "",
  "startDate": "",
  "endDate": ""
}
```
- update existing project (PUT /api/projects)
```
{
  "title": "",
  "description": "",
  "startDate": "",
  "endDate": ""
}
```
- assign project to user (POST /api/projects/projectid/assign/userid) __ids may be opposite__
- delete project (DELETE /api/projects/id)

- view all existing performances (GET /api/performances)
- assign user to project (POST /api/performances/assign)
```
{
  "userId": id,
  "projectId": id,
  "role": "",
  "specialization": "",
  "status": ""
}
```
- update performance for user on project (PUT /api/performances/projectid/userid) __ids may be opposite__
```
{
    "role": "",
    "specialization": "",
    "status": ""
}
```
- update performance just by performance id (PUT /api/performances/id)
```
{
    "role": "",
    "specialization": "",
    "status": ""
}
```
- view single performance for user on project (GET /api/performances/userid/projectid) __ids may be opposite__
- view all performances for user (GET /api/performances/userid) __error may occure__
- view all performances on project (GET /api/performances/projectid) __error may occure__

# Extras
- implement localization for hr (PerformanceStatus and ProjectStatus) __not available__

