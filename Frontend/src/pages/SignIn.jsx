import React from "react";
import { useNavigate } from "react-router-dom";
import { signIn } from "../api";

function randomUserId() {
  return Math.random().toString(36).slice(2, 10);
}

export default function SignIn() {
  const navigate = useNavigate();
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState(null);

  async function handleSignIn() {
    setLoading(true);
    setError(null);
    const username = randomUserId();
    try {
      const res = await signIn(username);
      localStorage.setItem("filmbox_userId", res.username);
      localStorage.setItem("filmbox_jwt", res.token);
      navigate("/");
    } catch (e) {
      setError("Sign in failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="flex items-center justify-center min-h-screen w-screen bg-gray-50">
      <div className="w-full max-w-md bg-white rounded shadow p-8 flex flex-col items-center">
        <h2 className="text-2xl font-bold mb-6">Sign In</h2>
        {error && <div className="text-red-500 mb-4">{error}</div>}
        <button
          onClick={handleSignIn}
          className="bg-blue-600 text-white text-lg px-8 py-4 rounded w-full"
          disabled={loading}
        >
          {loading ? "Signing in..." : "Sign In"}
        </button>
      </div>
    </div>
  );
}
