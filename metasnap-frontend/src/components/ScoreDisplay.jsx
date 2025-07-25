import React from "react";

const getScoreColor = (score) => {
  if (score >= 80) return "#22c55e"; // green-500
  if (score >= 60) return "#facc15"; // yellow-400
  return "#ef4444"; // red-500
};

const ScoreDisplay = ({ score }) => {
  const radius = 48;
  const stroke = 8;
  const normalizedRadius = radius - stroke / 2;
  const circumference = normalizedRadius * 2 * Math.PI;
  const percent = Math.max(0, Math.min(100, score));
  const strokeDashoffset = circumference - (percent / 100) * circumference;

  return (
    <div className="bg-white rounded-xl shadow p-6 flex flex-col items-center justify-center min-w-[180px] min-h-[180px]">
      <svg height={radius * 2} width={radius * 2} className="mb-2">
        <circle
          stroke="#e5e7eb"
          fill="transparent"
          strokeWidth={stroke}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
        />
        <circle
          stroke={getScoreColor(score)}
          fill="transparent"
          strokeWidth={stroke}
          strokeLinecap="round"
          strokeDasharray={circumference + " " + circumference}
          style={{ strokeDashoffset, transition: "stroke-dashoffset 0.5s" }}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
        />
        <text
          x="50%"
          y="50%"
          textAnchor="middle"
          dy=".3em"
          fontSize="2.2rem"
          fontWeight="bold"
          fill="#22223b"
        >
          {score}
        </text>
      </svg>
      <div className="text-gray-700 font-semibold text-lg tracking-wide">SEO SCORE</div>
    </div>
  );
};

export default ScoreDisplay;