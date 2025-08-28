import React from "react";

export default function Pagination({ page, totalPages, onPage }) {
  return (
    <div className="flex items-center justify-center gap-2 mt-6">
      <button
        className="px-2 py-1 rounded bg-gray-200"
        disabled={page === 0}
        onClick={() => onPage(page - 1)}
      >
        Previous
      </button>
      {Array.from({ length: totalPages }, (_, i) => (
        <button
          key={i}
          className={`px-3 py-1 rounded ${
            i === page ? "bg-indigo-500 text-white" : "bg-gray-100"
          }`}
          onClick={() => onPage(i)}
        >
          {i + 1}
        </button>
      ))}
      <button
        className="px-2 py-1 rounded bg-gray-200"
        disabled={page === totalPages - 1}
        onClick={() => onPage(page + 1)}
      >
        Next
      </button>
    </div>
  );
}
