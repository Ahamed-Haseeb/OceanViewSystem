<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ocean View Resort | Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;600&display=swap" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Inter', sans-serif; background-color: #f2f5f9; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; }
        .login-card { background: white; padding: 40px; border-radius: 15px; box-shadow: 0 10px 30px rgba(0,0,0,0.1); width: 100%; max-width: 400px; }
        .btn-primary { background: linear-gradient(135deg, #0061f2 0%, #6900cf 100%); border: none; padding: 12px; border-radius: 8px; width: 100%; font-weight: bold; transition: 0.3s; }
        .btn-primary:hover { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(0,97,242,0.4); }
    </style>
</head>
<body>

<div class="login-card text-center">
    <h3 class="fw-bold mb-2">🌊 Ocean View</h3>
    <p class="text-muted mb-4">Staff Login Portal</p>

    <% if(request.getParameter("error") != null) { %>
    <div class="alert alert-danger small py-2">Invalid Username or Password!</div>
    <% } %>

    <form action="login" method="post">
        <div class="mb-3 text-start">
            <label class="form-label small fw-bold">Username</label>
            <input type="text" name="username" class="form-control" required placeholder="Enter username">
        </div>
        <div class="mb-4 text-start">
            <label class="form-label small fw-bold">Password</label>
            <input type="password" name="password" class="form-control" required placeholder="Enter password">
        </div>
        <button type="submit" class="btn btn-primary text-white">Login to System</button>
    </form>
</div>

</body>
</html>