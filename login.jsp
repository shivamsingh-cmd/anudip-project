<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Grand | Staff Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:wght@300;400;600&family=Jost:wght@300;400;500&display=swap" rel="stylesheet">
    <style>
        * { box-sizing: border-box; margin: 0; padding: 0; }

        body {
            font-family: 'Jost', sans-serif;
            background: #0d0d0d;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            overflow: hidden;
        }

        /* Animated background */
        body::before {
            content: '';
            position: fixed;
            inset: 0;
            background:
                radial-gradient(ellipse 80% 60% at 20% 40%, rgba(180,140,90,0.12) 0%, transparent 60%),
                radial-gradient(ellipse 60% 80% at 80% 60%, rgba(140,100,60,0.08) 0%, transparent 60%);
            pointer-events: none;
        }

        .login-container {
            position: relative;
            width: 440px;
            padding: 60px 50px;
            background: rgba(255,255,255,0.03);
            border: 1px solid rgba(180,140,90,0.2);
            backdrop-filter: blur(20px);
            animation: fadeIn 0.8s ease;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to   { opacity: 1; transform: translateY(0); }
        }

        .login-container::before {
            content: '';
            position: absolute;
            top: 0; left: 10%; right: 10%; height: 1px;
            background: linear-gradient(to right, transparent, #b48c5a, transparent);
        }

        .hotel-logo {
            text-align: center;
            margin-bottom: 40px;
        }

        .hotel-logo .icon {
            font-size: 32px;
            margin-bottom: 12px;
            display: block;
        }

        .hotel-logo h1 {
            font-family: 'Cormorant Garamond', serif;
            font-size: 32px;
            font-weight: 300;
            color: #e8d5b0;
            letter-spacing: 4px;
            text-transform: uppercase;
        }

        .hotel-logo p {
            font-size: 11px;
            color: #b48c5a;
            letter-spacing: 3px;
            text-transform: uppercase;
            margin-top: 6px;
        }

        .divider {
            display: flex;
            align-items: center;
            gap: 12px;
            margin: 28px 0;
        }
        .divider::before, .divider::after {
            content: '';
            flex: 1;
            height: 1px;
            background: rgba(180,140,90,0.25);
        }
        .divider span {
            color: #b48c5a;
            font-size: 10px;
            letter-spacing: 3px;
            text-transform: uppercase;
        }

        .form-group { margin-bottom: 20px; }

        label {
            display: block;
            font-size: 11px;
            letter-spacing: 2px;
            text-transform: uppercase;
            color: #8a7560;
            margin-bottom: 8px;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 14px 16px;
            background: rgba(255,255,255,0.04);
            border: 1px solid rgba(180,140,90,0.2);
            color: #e8d5b0;
            font-family: 'Jost', sans-serif;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s;
        }

        input:focus {
            border-color: #b48c5a;
            background: rgba(180,140,90,0.05);
        }

        input::placeholder { color: #4a4030; }

        .btn-login {
            width: 100%;
            padding: 15px;
            background: linear-gradient(135deg, #b48c5a, #8a6a3e);
            color: #0d0d0d;
            font-family: 'Jost', sans-serif;
            font-size: 12px;
            font-weight: 500;
            letter-spacing: 3px;
            text-transform: uppercase;
            border: none;
            cursor: pointer;
            transition: opacity 0.3s, transform 0.2s;
            margin-top: 8px;
        }

        .btn-login:hover { opacity: 0.9; transform: translateY(-1px); }
        .btn-login:active { transform: translateY(0); }

        .error-msg {
            background: rgba(220,60,60,0.12);
            border: 1px solid rgba(220,60,60,0.3);
            color: #e07070;
            padding: 12px 16px;
            font-size: 13px;
            margin-bottom: 20px;
            letter-spacing: 0.3px;
        }

        .hint {
            text-align: center;
            margin-top: 24px;
            font-size: 11px;
            color: #4a4030;
            letter-spacing: 0.5px;
        }

        .hint span { color: #b48c5a; }
    </style>
</head>
<body>
<div class="login-container">
    <div class="hotel-logo">
        <span class="icon">🏨</span>
        <h1>Hotel Grand</h1>
        <p>Management System</p>
    </div>

    <div class="divider"><span>Staff Access</span></div>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error-msg">⚠ ${error}</div>
    <% } %>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label>Username</label>
            <input type="text" name="username" placeholder="Enter your username" required autofocus>
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" placeholder="••••••••" required>
        </div>
        <button type="submit" class="btn-login">Sign In</button>
    </form>

    <p class="hint">Default: <span>admin</span> / <span>Admin@123</span></p>
</div>
</body>
</html>
