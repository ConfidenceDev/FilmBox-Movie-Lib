import React from "react";
import MovieCard from "../components/MovieCard";
import Pagination from "../components/Pagination";
import { fetchMovies } from "../api";

export default function Home() {
  const [search, setSearch] = React.useState("");
  const [sortDir, setSortDir] = React.useState("asc");
  const [page, setPage] = React.useState(0);
  const [size] = React.useState(5);
  const [movies, setMovies] = React.useState([]);
  const [loading, setLoading] = React.useState(false);
  const [noResults, setNoResults] = React.useState(false);

  async function load() {
    setLoading(true);
    const searchBy = search.trim() === "" ? "all" : search;
    const res = await fetchMovies({
      page,
      size,
      sortBy: "id",
      sortDir,
      searchBy,
    });
    if (Array.isArray(res) && res.length === 1 && res[0].message) {
      setMovies([]);
      setNoResults(true);
    } else {
      setMovies(res);
      setNoResults(false);
    }
    setLoading(false);
  }

  React.useEffect(() => {
    load();
    // eslint-disable-next-line
  }, [page, sortDir]);

  function onSearchSubmit(e) {
    e.preventDefault();
    setPage(0);
    load();
  }

  // For demo, assume 10 pages max (should be from backend)
  const totalPages = 10;

  return (
    <div>
      <div className="mb-4">
        <form onSubmit={onSearchSubmit} className="flex gap-2">
          <input
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search movies, actors, genres, tags..."
            className="flex-1 p-2 rounded border"
          />
          <select
            value={sortDir}
            onChange={(e) => setSortDir(e.target.value)}
            className="p-2 rounded border"
          >
            <option value="asc">Ascending</option>
            <option value="desc">Descending</option>
          </select>
          <button
            type="submit"
            className="bg-indigo-600 text-white px-4 py-2 rounded"
          >
            Search
          </button>
        </form>
      </div>
      {loading && <div className="text-center py-8">Loading...</div>}
      {noResults && (
        <div className="text-center py-8 text-gray-500">No movies found.</div>
      )}
      <div className="space-y-4">
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div>
      <Pagination page={page} totalPages={totalPages} onPage={setPage} />
    </div>
  );
}
