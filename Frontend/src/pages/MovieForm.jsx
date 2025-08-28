import React from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createMovie, updateMovie, fetchMovie, getUserId } from "../api";

export default function MovieForm({ edit }) {
  const { id } = useParams();
  const navigate = useNavigate();
  const userId = getUserId();

  const [form, setForm] = React.useState({
    title: "",
    summary: "",
    year: "",
    genre: "",
    actors: "",
    tags: "",
  });
  const [loading, setLoading] = React.useState(false);
  const [error, setError] = React.useState(null);

  React.useEffect(() => {
    if (edit && id) {
      fetchMovie(id).then((movie) => {
        setForm({
          title: movie.title || "",
          summary: movie.summary || "",
          year: movie.year || "",
          genre: movie.genre || "",
          actors: movie.actors?.join(", ") || "",
          tags: movie.tags?.join(", ") || "",
        });
      });
    }
  }, [edit, id]);

  function handleChange(e) {
    setForm((f) => ({ ...f, [e.target.name]: e.target.value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError(null);
    const movieRequest = {
      posterId: userId,
      title: form.title,
      summary: form.summary,
      year: Number(form.year),
      genre: form.genre,
      actors: form.actors
        .split(",")
        .map((a) => a.trim())
        .filter(Boolean),
      tags: form.tags
        .split(",")
        .map((t) => t.trim())
        .filter(Boolean),
    };
    try {
      if (edit) {
        await updateMovie(id, movieRequest);
      } else {
        await createMovie(movieRequest);
      }
      navigate("/");
    } catch (e) {
      setError("Failed to save movie");
    } finally {
      setLoading(false);
    }
  }

  if (!userId) {
    return (
      <div className="text-center py-8 text-red-500">
        Sign in to add or edit movies.
      </div>
    );
  }

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white rounded shadow p-6 max-w-xl mx-auto"
    >
      <h2 className="text-xl font-bold mb-4">
        {edit ? "Update Movie" : "Add Movie"}
      </h2>
      {error && <div className="text-red-500 mb-2">{error}</div>}
      <div className="mb-2">
        <label className="block mb-1">Title</label>
        <input
          name="title"
          value={form.title}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
      </div>
      <div className="mb-2">
        <label className="block mb-1">Summary</label>
        <textarea
          name="summary"
          value={form.summary}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          rows={3}
          required
        />
      </div>
      <div className="mb-2">
        <label className="block mb-1">Year</label>
        <input
          name="year"
          type="number"
          value={form.year}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
      </div>
      <div className="mb-2">
        <label className="block mb-1">Genre</label>
        <input
          name="genre"
          value={form.genre}
          onChange={handleChange}
          className="w-full p-2 border rounded"
          required
        />
      </div>
      <div className="mb-2">
        <label className="block mb-1">Actors (comma separated)</label>
        <input
          name="actors"
          value={form.actors}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        />
      </div>
      <div className="mb-4">
        <label className="block mb-1">Tags (comma separated)</label>
        <input
          name="tags"
          value={form.tags}
          onChange={handleChange}
          className="w-full p-2 border rounded"
        />
      </div>
      <button
        type="submit"
        className="bg-indigo-600 text-white px-4 py-2 rounded w-full"
        disabled={loading}
      >
        {edit ? "Update" : "Create"}
      </button>
    </form>
  );
}
