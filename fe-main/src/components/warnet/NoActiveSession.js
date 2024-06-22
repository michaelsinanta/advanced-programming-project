import Link from "next/link";

export default function NoActiveSession() {
  return (
    <div className="flex flex-col items-center justify-center h-[calc(100vh-5rem)]">
      <h1 className="py-10">
        Waduh, sepertinya Anda tidak sedang memiliki sesi aktif!
      </h1>
      <Link
        href="/"
        className="bg-blue-500 px-4 py-2 rounded text-white font-medium"
      >
        Pesan sesi warnet
      </Link>
      <div className="pb-20"></div>
    </div>
  );
}
