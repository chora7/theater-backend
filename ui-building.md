# 15-Day Plan
Day 1 — Audit & decide the auth strategy

Review SecurityConfig.java, LoginController.java, UserController.java in full
Decide: session-based auth (simpler for Thymeleaf, no manual token handling) vs JWT-in-cookie (keeps your REST API and UI unified)
My recommendation once I see the files: session-based for Thymeleaf pages, keep JWT only for pure API clients if needed later
Fix/confirm /register endpoint exists and works

Day 2 — Registration page

Build register.html (Thymeleaf, styled like login.html)
Wire to registration endpoint, test creating both USER and ADMIN accounts

Day 3 — Fix and finalize login flow

Get login.html fully working end-to-end with real credentials
Confirm role-based redirect (admin → admin dashboard, user → user dashboard)
Add proper logout button/link

Day 4 — Base layout & navigation

Create a shared Thymeleaf layout (header/nav/footer) using th:insert/th:replace fragments
Add role-aware nav (show "Manage Users" only for admins, etc.)

Day 5 — Admin dashboard: view all projects

Build dashboard.html for admin listing all projects (using your existing GET /api/projects)
Handle the {message, data} response wrapper in Thymeleaf model attributes

Day 6 — Admin: create project form

Build "Add Project" form page
Wire to POST /api/projects
Handle validation errors and success messages

Day 7 — Admin: edit/delete project

Build edit form (pre-filled from existing project)
Wire to update/delete endpoints (check if these exist yet — may need backend work)

Day 8 — User assignment to projects

Design UI for assigning users to a project (dropdown/multi-select)
This likely needs new backend endpoints — we'll check ProjectService/ProjectController for what exists

Day 9 — Regular user dashboard

Build simplified view: user sees only their assigned projects
Read-only or limited-edit view depending on your requirements

Day 10 — Department & Location management (admin)

I saw DepartmentController and LocationController in your files — build basic CRUD UI for these if needed

Day 11 — Performance module UI

I saw PerformanceController/PerformanceService — build whatever UI this needs (unclear scope yet, we'll clarify)

Day 12 — Polish & error handling

Consistent error messages across all forms
Loading/empty states
Basic CSS pass for usability

Day 13 — Edge cases & bug sweep

Test all role combinations (admin vs user access control)
Fix any 403/500s discovered
Re-run and fix your existing test suite if things broke

Day 14 — Full end-to-end manual QA

Walk through every user story: register → login → admin creates project → assigns user → user views it → logout

Day 15 — Buffer / documentation

Catch-up day for anything that overran
Write a short README on how to run the app and what's implemented
