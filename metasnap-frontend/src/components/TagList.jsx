import React from "react";

const feedbackColor = {
  good: "bg-green-100 text-green-700 border-green-300",
  warning: "bg-yellow-100 text-yellow-700 border-yellow-300",
  missing: "bg-red-100 text-red-700 border-red-300",
};

const formatValue = (value) => {
  if (value === null || value === undefined || value === '') {
    return "Missing";
  }
  if (typeof value === "object") {
    return JSON.stringify(value);
  }
  return String(value);
};

const TagList = ({ tags, feedback }) => (
  <div className="bg-gray-50 rounded-xl p-5 shadow border">
    <h2 className="font-bold text-lg mb-3 text-blue-700">Meta Tags</h2>
    <ul className="mb-4 space-y-1">
      {tags &&
        Object.entries(tags).map(([key, value]) => {
          const isPresent = value !== null && value !== undefined;
          const displayValue = formatValue(value);
          return (
            <li key={key} className="flex justify-between items-center">
              <span className="capitalize text-gray-700">{key.replace(/([A-Z])/g, " $1")}</span>
              <span className={`ml-2 px-2 py-1 rounded text-xs ${isPresent ? "bg-green-100 text-green-700" : "bg-red-100 text-red-700"}`}>
                {displayValue}
              </span>
            </li>
          );
        })}
    </ul>
    <h3 className="font-semibold mb-2 text-gray-700">Feedback</h3>
    <ul className="space-y-1">
      {feedback &&
        feedback.map((item, idx) => (
          <li
            key={idx}
            className={`border-l-4 pl-2 py-1 rounded ${feedbackColor[item.type] || "bg-gray-100 text-gray-700 border-gray-300"}`}
          >
            {item.message}
          </li>
        ))}
    </ul>
  </div>
);

export default TagList;