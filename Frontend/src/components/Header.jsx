import React from "react";
import { Link, useNavigate } from "react-router-dom";
import { signOut, getUserId } from "../api";

export default function Header() {
  const navigate = useNavigate();
  const [user, setUser] = React.useState(getUserId());

  React.useEffect(() => {
    function onStorage() {
      setUser(getUserId());
    }
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
  }, []);

  async function handleSignOut() {
    try {
      await signOut();
    } catch (e) {}
    localStorage.removeItem("filmbox_jwt");
    localStorage.removeItem("filmbox_userId");
    setUser(null);
    navigate("/");
  }

  return (
    <header className="bg-white shadow">
      <div className="max-w-4xl mx-auto flex items-center justify-between p-4">
        <Link to="/" className="flex items-center gap-3">
          <div className="w-10 h-10 bg-indigo-600 rounded flex items-center justify-center text-white font-bold text-lg">
            FB
          </div>
          <div>
            <div className="text-xl font-bold">FilmBox</div>
            <div className="text-xs text-gray-500">Movie Library</div>
          </div>
        </Link>
        <div className="flex items-center gap-3">
          <div className="text-sm">{user ?? "guest"}</div>
          <Link to="/add">
            <button
              className="bg-green-500 text-white px-3 py-1 rounded disabled:opacity-50"
              disabled={!user}
            >
              Add movie
            </button>
          </Link>
          {user ? (
            <button
              onClick={handleSignOut}
              className="bg-red-500 text-white px-3 py-1 rounded"
            >
              Sign out
            </button>
          ) : (
            <Link to="/signin">
              <button className="bg-blue-600 text-white px-3 py-1 rounded">
                Sign in
              </button>
            </Link>
          )}
        </div>
      </div>
    </header>
  );
}
