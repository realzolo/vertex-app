export function appendApiPrefix(filePath: string) {
  return (import.meta.env.VITE_API_PREFIX ?? '') + filePath
}
