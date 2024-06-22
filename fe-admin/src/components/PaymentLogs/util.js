export function modalTitle(filterBy) {
  if (filterBy === "yearly") return "Filter Yearly";
  if (filterBy === "monthly") return "Filter Monthly";
  return "Filter Weekly";
}
