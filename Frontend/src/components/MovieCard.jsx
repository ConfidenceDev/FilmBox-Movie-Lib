import React from "react";
import { Link } from "react-router-dom";

export default function MovieCard({ movie }) {
  const shortSummary =
    movie.summary && movie.summary.length > 180
      ? movie.summary.slice(0, 177) + "..."
      : movie.summary || "";
  const created = movie.createdAt
    ? new Date(movie.createdAt).toLocaleString()
    : "";
  return (
    <Link
      to={`/movie/${movie.id}`}
      className="block bg-white p-4 rounded shadow hover:shadow-md"
    >
      <div className="flex justify-between">
        <h3 className="text-lg font-semibold">{movie.title ?? "Untitled"}</h3>
        <div className="text-sm text-gray-500">{movie.year ?? ""}</div>
      </div>
      <p className="text-sm text-gray-700 mt-2">{shortSummary}</p>
      <div className="mt-3 flex flex-wrap gap-2 text-xs">
        {movie.genre && (
          <span className="px-2 py-0.5 bg-indigo-100 text-indigo-700 rounded">
            {movie.genre}
          </span>
        )}
        {movie.actors?.slice(0, 5).map((a, i) => (
          <span key={i} className="px-2 py-0.5 bg-gray-100 rounded">
            {a}
          </span>
        ))}
        {movie.tags?.slice(0, 5).map((t, i) => (
          <span key={i} className="px-2 py-0.5 bg-yellow-100 rounded">
            {t}
          </span>
        ))}
      </div>
      <div className="text-xs text-gray-400 mt-3">Posted: {created}</div>
    </Link>
  );
}
