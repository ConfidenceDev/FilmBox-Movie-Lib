const API_BASE = "/api/v1";

function getToken() {
  return localStorage.getItem("filmbox_jwt");
}

function getUserId() {
  return localStorage.getItem("filmbox_userId");
}

function authHeaders() {
  const token = getToken();
  return token
    ? { Authorization: `Bearer ${token}`, "Content-Type": "application/json" }
    : { "Content-Type": "application/json" };
}

export async function signIn(username) {
  const res = await fetch(`${API_BASE}/signIn`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(username),
  });
  if (!res.ok) throw new Error("Sign in failed");
  return res.json();
}

export async function signOut() {
  const token = getToken();
  const res = await fetch(`${API_BASE}/signOut`, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.json();
}

export async function fetchMovies({
  page = 0,
  size = 5,
  sortBy = "id",
  sortDir = "asc",
  searchBy = "all",
}) {
  const q = new URLSearchParams({ page, size, sortBy, sortDir, searchBy });
  const res = await fetch(`${API_BASE}/movies?${q.toString()}`, {
    headers: authHeaders(),
  });
  return res.json();
}

export async function fetchMovie(id) {
  const res = await fetch(`${API_BASE}/movies/${id}`, {
    headers: authHeaders(),
  });
  return res.json();
}

export async function createMovie(movieRequest) {
  const res = await fetch(`${API_BASE}/movies`, {
    method: "POST",
    headers: authHeaders(),
    body: JSON.stringify(movieRequest),
  });
  return res.json();
}

export async function updateMovie(id, movieRequest) {
  const res = await fetch(`${API_BASE}/movies/${id}`, {
    method: "PUT",
    headers: authHeaders(),
    body: JSON.stringify(movieRequest),
  });
  return res.text();
}

export async function deleteMovie(id) {
  const res = await fetch(`${API_BASE}/movies/${id}`, {
    method: "DELETE",
    headers: { Authorization: authHeaders().Authorization },
  });
  return res.json();
}

export { getToken, getUserId };
