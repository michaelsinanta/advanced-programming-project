export const numberToRupiah = (a) => {
    if (a === undefined) return
    return new Intl.NumberFormat('id-ID', {
      style: 'currency',
      currency: 'IDR',
      minimumFractionDigits: 0,
    })
      .format(a)
      .replace(/\s/g, '')
  }