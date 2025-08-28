import React from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import { fetchMovie, deleteMovie, getUserId } from "../api";

export default function MovieDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [movie, setMovie] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const [error, setError] = React.useState(null);
  const userId = getUserId();

  React.useEffect(() => {
    fetchMovie(id)
      .then(setMovie)
      .catch((e) => setError("Movie not found"))
      .finally(() => setLoading(false));
  }, [id]);

  async function handleDelete() {
    if (!window.confirm("Delete this movie?")) return;
    await deleteMovie(id);
    navigate("/");
  }

  if (loading) return <div className="text-center py-8">Loading...</div>;
  if (error)
    return <div className="text-center py-8 text-red-500">{error}</div>;
  if (!movie) return null;

  return (
    <div className="bg-white rounded shadow p-6">
      <h2 className="text-2xl font-bold mb-2">{movie.title}</h2>
      <div className="mb-2 text-gray-600">
        {movie.year} &middot; {movie.genre}
      </div>
      <div className="mb-4">{movie.summary}</div>
      <div className="mb-2">
        <span className="font-semibold">Actors:</span>{" "}
        {movie.actors?.join(", ") || "N/A"}
      </div>
      <div className="mb-2">
        <span className="font-semibold">Tags:</span>{" "}
        {movie.tags?.join(", ") || "N/A"}
      </div>
      <div className="mb-2 text-xs text-gray-400">
        Posted:{" "}
        {movie.createdAt ? new Date(movie.createdAt).toLocaleString() : ""}
      </div>
      {userId && movie.posterId === userId && (
        <div className="flex gap-2 mt-6">
          <Link to={`/movie/${movie.id}/edit`}>
            <button className="bg-indigo-600 text-white px-4 py-2 rounded">
              Update
            </button>
          </Link>
          <button
            onClick={handleDelete}
            className="bg-red-500 text-white px-4 py-2 rounded"
          >
            Delete
          </button>
        </div>
      )}
    </div>
  );
}
