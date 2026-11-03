/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        dark: {
          900: '#0b0f19',
          850: '#0f172a',
          800: '#1e293b',
          700: '#334155',
          600: '#475569',
        },
        sports: {
          pitch: '#10B981',
          gold: '#F59E0B',
          red: '#EF4444',
          blue: '#3B82F6',
          purple: '#8B5CF6',
        },
        gpt: {
          primary: '#10A37F',
          dark: '#0e8065',
          light: '#d1fae5',
        },
        qwen: {
          primary: '#6366F1',
          dark: '#4f46e5',
          light: '#e0e7ff',
        }
      },
      fontFamily: {
        sans: ['Outfit', 'Inter', 'system-ui', '-apple-system', 'sans-serif'],
      },
      boxShadow: {
        'glow-emerald': '0 0 25px -5px rgba(16, 185, 129, 0.3)',
        'glow-gold': '0 0 25px -5px rgba(245, 158, 11, 0.3)',
        'glow-gpt': '0 0 25px -5px rgba(16, 163, 127, 0.3)',
        'glow-qwen': '0 0 25px -5px rgba(99, 102, 241, 0.3)',
      }
    },
  },
  plugins: [],
}
