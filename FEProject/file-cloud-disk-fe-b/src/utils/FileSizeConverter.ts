export const sizeTostr = (size: number): string => {
  const KB = 1024
  const MB = KB * 1024
  const GB = MB * 1024
  const TB = GB * 1024

  let data = ""

  if (size < 0.1 * KB) {
    data = size.toFixed(2) + "B"
  } else if (size < 0.1 * MB) {
    data = (size / KB).toFixed(2) + "KB"
  } else if (size < GB * 0.8) {
    data = (size / MB).toFixed(2) + "MB"
  } else if (size < TB * 0.8) {
    data = (size / GB).toFixed(2) + "GB"
  } else {
    data = (size / TB).toFixed(2) + "TB"
  }

  const len = data.indexOf(".")
  const dec = data.substring(len + 1, len + 3)
  if (dec === "00") {
    return data.substring(0, len) + data.substring(len + 3, len + 5)
  }
  return data
} 