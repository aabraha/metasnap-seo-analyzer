import React from "react";

const icons = {
  good: (
    <svg className="w-5 h-5 text-green-500 mr-2" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" /></svg>
  ),
  warning: (
    <svg className="w-5 h-5 text-yellow-500 mr-2" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M12 8v4m0 4h.01M21 12A9 9 0 1 1 3 12a9 9 0 0 1 18 0z" /></svg>
  ),
  missing: (
    <svg className="w-5 h-5 text-red-500 mr-2" fill="none" stroke="currentColor" strokeWidth="2" viewBox="0 0 24 24"><path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" /></svg>
  ),
};

const FeedbackCard = ({ feedback }) => (
  <div className="bg-white rounded-xl shadow p-6 min-h-[180px] flex flex-col">
    <h2 className="font-bold text-lg mb-3">Suggestions</h2>
    <ul className="space-y-2 flex-1">
      {feedback && feedback.length > 0 ? (
        feedback.map((item, idx) => (
          <li key={idx} className="flex items-center text-sm">
            {icons[item.type] || null}
            <span
              className={
                item.type === "good"
                  ? "text-green-700"
                  : item.type === "warning"
                  ? "text-yellow-700"
                  : "text-red-700"
              }
            >
              {item.message}
            </span>
          </li>
        ))
      ) : (
        <li className="text-gray-400">No suggestions</li>
      )}
    </ul>
  </div>
);

export default FeedbackCard; 