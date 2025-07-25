import React from "react";

const PreviewCard = ({ type, tags }) => {
  if (type === "Google") {
    return (
      <div className="bg-white rounded-xl shadow border p-5 flex flex-col min-h-[160px]">
        <div className="text-xs text-gray-400 mb-1 font-semibold">Google Preview</div>
        <div className="text-blue-700 text-lg font-bold underline leading-tight truncate mb-1 cursor-pointer hover:text-blue-800">{tags?.title || "Title"}</div>
        <div className="text-green-700 text-sm mb-1">{tags?.canonical || "www.example.com"}</div>
        <div className="text-gray-700 text-sm">{tags?.description || "Description will appear here."}</div>
      </div>
    );
  }
  if (type === "Twitter") {
    return (
      <div className="bg-white rounded-xl shadow border p-5 flex flex-col min-h-[160px]">
        <div className="text-xs text-blue-400 mb-1 font-semibold">Twitter Preview</div>
        <div className="flex items-center gap-2 mb-2">
          <div className="w-8 h-8 rounded-full bg-blue-300 flex items-center justify-center text-white font-bold">T</div>
          <span className="font-bold text-blue-700">@yourhandle</span>
        </div>
        <div className="text-gray-900 font-bold">{tags?.twitterTitle || "Twitter Title"}</div>
        <div className="text-gray-600">{tags?.twitterDescription || "Twitter description will appear here."}</div>
        <div className="text-xs text-gray-400 mt-2">{tags?.canonical || "example.com"}</div>
      </div>
    );
  }
  if (type === "Facebook") {
    return (
      <div className="bg-white rounded-xl shadow border p-5 flex flex-col min-h-[160px]">
        <div className="text-xs text-blue-700 mb-1 font-semibold">Facebook Preview</div>
        <div className="flex items-center gap-2 mb-2">
          <div className="w-8 h-8 rounded-full bg-blue-500 flex items-center justify-center text-white font-bold">F</div>
          <span className="font-bold text-blue-700">MetaSnap</span>
        </div>
        <div className="text-gray-900 font-bold">{tags?.ogTitle || "Open Graph Title"}</div>
        <div className="text-gray-700">{tags?.ogDescription || "Open Graph description will appear here."}</div>
        <div className="text-xs text-gray-400 mt-2">{tags?.canonical || "example.com"}</div>
      </div>
    );
  }
  // Fallback
  return (
    <div className="bg-gray-100 rounded-lg shadow border p-4">
      <div className="text-xs text-gray-400 mb-1">{type} Preview</div>
      <div className="text-gray-700">No preview available.</div>
    </div>
  );
};

export default PreviewCard;