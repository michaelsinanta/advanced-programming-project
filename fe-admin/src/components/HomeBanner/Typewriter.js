import { useState, useEffect } from 'react'

export const Typewriter = ({ text }) => {
    const [currentText, setCurrentText] = useState('')
    const [index, setIndex] = useState(0)

    useEffect(() => {
        if (index < text.length) {
            const timeoutId = setTimeout(() => {
                setCurrentText(currentText + text.charAt(index))
                setIndex(index + 1)
            }, 100)
            return () => clearTimeout(timeoutId)
        }
    }, [currentText, index, text])

    return (
        <h1 className="text-6xl font-bold text-center">
            {currentText}
            <span className="animate-ping inline-block ml-2 h-2 w-2 rounded-full bg-purple-500"></span>
        </h1>
    )
}