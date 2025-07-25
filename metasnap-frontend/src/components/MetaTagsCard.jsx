import React from "react";

const getColor = (value) => {
  if (!value) return "text-yellow-500 font-semibold";
  return "text-gray-800";
};

const MetaTagsCard = ({ tags }) => (
  <div className="bg-white rounded-xl shadow p-6 min-h-[180px] flex flex-col">
    <h2 className="font-bold text-lg mb-3">SEO META TAGS</h2>
    <div className="grid grid-cols-2 gap-y-2 gap-x-4 text-sm flex-1">
      <div className="font-semibold">Title</div>
      <div className={getColor(tags?.title)}>{tags?.title || "Not specified"}</div>
      <div className="font-semibold">Description</div>
      <div className={getColor(tags?.description)}>{tags?.description || "Not specified"}</div>
      <div className="font-semibold">Canonical URL</div>
      <div className={getColor(tags?.canonical)}>{tags?.canonical || "Not specified"}</div>
      <div className="font-semibold">Open Graph</div>
      <div className={getColor(tags?.ogTitle)}>{tags?.ogTitle || "Not specified"}</div>
    </div>
  </div>
);

export default MetaTagsCard; 