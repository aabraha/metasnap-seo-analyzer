import React, { useState } from "react";
import ScoreDisplay from "./ScoreDisplay";
import FeedbackCard from "./FeedbackCard";
import MetaTagsCard from "./MetaTagsCard";
import PreviewCard from "./PreviewCard";

const MetaAnalyzer = () => {
  const [url, setUrl] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleAnalyze = async (e) => {
    e.preventDefault();
    setResult(null);
    setLoading(true);
    try {
      const res = await fetch("http://localhost:8080/api/analyze", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ url }),
      });
      if (!res.ok) throw new Error("API error");
      const data = await res.json();
      setResult(data);
    } catch (err) {
      alert("Failed to analyze URL. Is the backend running?");
    }
    setLoading(false);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-purple-100 flex flex-col items-center py-10 font-sans">
      <div className="w-full max-w-6xl bg-white rounded-2xl shadow-xl p-8">
        {/* Input Bar Card */}
        <form
          onSubmit={handleAnalyze}
          className="flex justify-center mb-12"
        >
          <div className="flex w-full max-w-2xl bg-white rounded-full shadow border border-gray-200 overflow-hidden">
            <input
              type="url"
              className="flex-1 px-6 py-4 text-lg bg-white focus:outline-none rounded-l-full"
              placeholder="https://example.com"
              value={url}
              onChange={(e) => setUrl(e.target.value)}
              required
              disabled={loading}
              style={{ minWidth: 0 }}
            />
            <button
              type="submit"
              className="bg-blue-600 hover:bg-blue-700 text-white font-semibold px-8 py-4 text-lg transition rounded-r-full focus:outline-none"
              disabled={loading}
              style={{ minWidth: '120px' }}
            >
              {loading ? "Analyzing..." : "Analyze"}
            </button>
          </div>
        </form>
        {result && (
          <>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
              <div className="flex flex-col h-full"><ScoreDisplay score={result.score} /></div>
              <div className="flex flex-col h-full"><FeedbackCard feedback={result.feedback} /></div>
              <div className="flex flex-col h-full"><MetaTagsCard tags={result.meta} /></div>
            </div>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <PreviewCard type="Google" tags={result.meta} />
              <PreviewCard type="Facebook" tags={result.meta} />
              <PreviewCard type="Twitter" tags={result.meta} />
            </div>
          </>
        )}
      </div>
      <footer className="mt-10 text-gray-400 text-xs text-center">
        &copy; {new Date().getFullYear()} MetaSnap &mdash; SEO Meta Tag Analyzer
      </footer>
    </div>
  );
};

export default MetaAnalyzer;